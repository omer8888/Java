package com.hit.util;

import java.util.Observable;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.String;

public class CLI extends Observable implements Runnable {

	private InputStream in;
	private OutputStream writer;
	private boolean serverStatus; //server already running?
	
	public CLI(InputStream in, OutputStream out) {
		this.in = in;
		this.writer = out;
		serverStatus = false;
	}

	public void write(String string) {
		try {
			writer.write((string + "\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//working after CacheUnitServerDriver telling this to start as a new thread
	@Override
	public void run() {
		Scanner userInput = new Scanner(in);    // get the inputs from the in 
												// was defined in CacheUnitServerDriver @@@@ ???? 
		while(true)	{
			write("Please enter your command");
			String inputString = userInput.next();
							//upper || lower
			if (inputString.equalsIgnoreCase("start") == true && serverStatus == false) {
				write("Starting server.......");
				serverStatus = true;
				setChanged();
				notifyObservers(inputString);	// tells the observers: server, the input
												// he will do his update(string) that do start()
												// server start() is telling the server to wait for connection
			} else if (inputString.equalsIgnoreCase("shutdown") == true
					|| inputString.equalsIgnoreCase("stop") == true) {
				write("Shutdown server");
				serverStatus = false;
				setChanged();
				notifyObservers(inputString);
			} else
				write("Not a valid command");
		}
		// userInput.close(); // WHERE TO PUT ?
	}
}
