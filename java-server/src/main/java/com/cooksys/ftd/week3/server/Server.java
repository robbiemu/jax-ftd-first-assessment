package com.cooksys.ftd.week3.server;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.App;
import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;

import static com.cooksys.ftd.week3.db.DBConnection.*;

public class Server implements Runnable {
	public static final int SERVER_PORT = 667;
	private Logger log = LoggerFactory.getLogger(Server.class);

	private ServerSocket serverSocket;

	@Override
	public void run() {		
		try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
			serverSocket = server;
			
			Class.forName(DRIVER);
			DBConnection.connection = DriverManager.getConnection(
					URL, DB_USERNAME, DB_PASSWORD);
						
			while (true) {				
				Socket socket = this.serverSocket.accept();
				ClientHandler handler = this.createClientHandler(socket);

				App.executor.execute(handler);
			}
		} catch (IOException e) {
			this.log.error("The server encountered a fatal error while listening for more connections. Shutting down after error log.", e);
		} catch (SQLException | ClassNotFoundException e) {
			this.log.error("The server encountered a fatal error while establishing connection to database. Shutting down after error log.", e);
			e.printStackTrace();
		} finally {
			try {
				DBConnection.getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ClientHandler createClientHandler(Socket socket) throws IOException {
		ClientHandler handler = new ClientHandler(socket);

		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		handler.setReader(reader);
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		handler.setWriter(writer);

		handler.setUserDao(userDao);
		handler.setFileDao(fileDao);

		return handler;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public FileDao getFileDao() {
		return fileDao;
	}

	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
