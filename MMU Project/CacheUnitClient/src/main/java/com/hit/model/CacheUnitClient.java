package com.hit.model;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class CacheUnitClient {

	private boolean succeed;
	private final static String HOST = "localhost";
	private final static int PORT = 12345;
	private Socket socket;
	
	public CacheUnitClient() { // C'tor
		System.out.println("In: CacheUnitClient C'tor");
		succeed = false;
	}
			
	public String send(String request) {
		System.out.println("In: CacheUnitClient 'send'");
		
		try	{ 
			
			System.out.println(1 + request);
			
			socket = new Socket(HOST, PORT);
			
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			System.out.println(2 + request);
			
			output.writeObject(request);
			output.flush();
			
			System.out.println("Message from server:");
			Object response = input.readObject();
			
			System.out.println(response);
			
			input.close();
			output.close();
			socket.close();
			
			if(response.toString().equals("true")) {
				succeed = true;
				return "Succeeded!";
			} 
			
			else if(response.toString().equals("false")) {
				succeed = false;
				return "Failed!";
			}
			
			else { // Statistics request
				return (String)response;
			}
					
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "";
	}
}
