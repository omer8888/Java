package com.hit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import com.hit.services.CacheUnitController;
////////////////////////////////////////////////////////////////////////////////////////
public class Server implements Observer {
	private ServerSocket ss;
	private String command;
	private boolean serverStatus;
	private CacheUnitController<String> cuc;
	private Socket someClient;
	private Calendar cal;
	
	public Server() { 
		try {					//port
			ss = new ServerSocket(12345); //open new server
			serverStatus = false;
			cuc = new CacheUnitController<String>();
			
			cal = Calendar.getInstance();
					
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void start() {
		ObjectOutputStream outToClient = null;
		try {	
			while(serverStatus == true) {
				someClient = ss.accept(); // waits until client connects (Blocking)
				System.out.println("A client connected at: " 
					+ new SimpleDateFormat("HH:mm:ss").format(cal.getTime()) 
					+ " from: " + someClient.getInetAddress());
				if(serverStatus == true) {
					System.out.println("@@@@@@@");
					new Thread(new HandleRequest<String>(someClient, cuc)).start();
				}
				// make new thread each time with different socket/(request?)
				else { // if server stop handlying requests, send a msg to client
					outToClient = new ObjectOutputStream(someClient.getOutputStream());
					outToClient.writeObject("Server not responding");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
						//CLI
	//Get here when his subject(CLI) notifyObservers()
	@Override
	public void update(Observable arg0, Object string) {
		command = (String) string;
		if (command.equalsIgnoreCase("start") == true) {
			serverStatus = true;
			Runnable runInNewThread = new Runnable() { 
				public void run() 
					{ start(); }
			};
			new Thread(runInNewThread).start(); // start() will run in its own thread
		}
		else if (command.equalsIgnoreCase("start") != true 
									&& serverStatus == true) {
			serverStatus = false;

		}
	}
}
