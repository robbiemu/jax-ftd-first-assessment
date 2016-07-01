package com.cooksys.ftd.week3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.server.Server;

public class App {
	private static Logger log = LoggerFactory.getLogger(App.class);
	public static Server server;

	public static ExecutorService executor;

	public static void main(String[] args) {
		executor = Executors.newCachedThreadPool(); // initialize
		
		try {			
			server = new Server();
			executor.execute(server);
		} catch (Exception e) {
			log.error("Server or database encountered a critical error, application halted.");
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
