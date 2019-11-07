package com.hit.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

// got here from the server
// when new connection has happened

public class HandleRequest<T> implements Runnable {

	private Socket s;           // the connection of the client
	CacheUnitController<T> controller;	// what operations we can do
	private boolean success; // for UPDATE and DELETE operations
	private DataModel<T>[] arr; // for GET operation
	
	public HandleRequest(Socket s, CacheUnitController<T> controller) { // C'tor
		this.s = s;
		this.controller = controller;
		success = false;
		System.out.println("@@@ In: handle request C'tor @@@");
	}

	@Override
	public void run() {
		System.out.println("@@@ In: handle request run() @@@");
		Scanner reader = null; 
		PrintWriter writer = null;
		String req = "";
		try {
			ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(s.getInputStream());
			
			reader = new Scanner(new InputStreamReader(s.getInputStream())); // input from socket
			writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream())); // output to socket
			
			req = (String)input.readObject();  // Yaniv used something like read UTF

			if (req.isEmpty())
				System.out.println("String is null");
			else
				System.out.println(req);
			
			// parsing gson correctly (by Nisim's PDF instructions)
			Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
			Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);
			
			System.out.println("Action type: " + request.getHeaders().get("action"));

			// check the command and doing it 
			
			if (request.getHeaders().get("action").equals("UPDATE")) {    // [action]="update"
				System.out.println("@@ In: HandleRequest UPDATE");
				success = controller.update(request.getBody());      // updating the []DM

				System.out.println("Update status: " + success);
				output.writeObject(success);
				output.flush();
			}
			else if (request.getHeaders().get("action").equals("DELETE")) {
				System.out.println("@@ In: HandleRequest DELETE");
				success = controller.delete(request.getBody());
				
				System.out.println("Deletion status: " + success);
				output.writeObject(success);
				output.flush();

			}
			else if (request.getHeaders().get("action").equals("GET")) {
				System.out.println("@@ In: HandleRequest GET");
				arr = controller.get(request.getBody());
				String tostring = "";
				
				System.out.println("Get status (array): ");
				for(DataModel<T> m : arr) {
						System.out.println(m.getDataModelId() + " " 
										 + m.getContent());
						tostring += m.getDataModelId() + " " + m.getContent() + ",";
				}
				
				String msg = new Gson().toJson(tostring);
				output.writeObject(msg);
				output.flush();
				
			}
			
			else if (request.getHeaders().get("action").equals("Statistics")) {
				System.out.println("@@ In: HandleRequest Statistics");
				
				String msg = controller.getStatistics();
				System.out.println("Statistics msg: " + msg);
				output.writeObject(msg);
				output.flush();
			}	
	
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
				reader.close();
				writer.close();
		}
	}
}
