package com.hit.server;

import com.hit.util.CLI;

public class CacheUnitServerDriver {

	public static void main(String[] args) {

		CLI cli = new CLI(System.in, System.out);
		Server server = new Server();
		cli.addObserver(server); // server becomes observer of CLI (server "listens" to CLI)				
		new Thread(cli).start(); // CLI starts as a new thread
	}
}
