package com.cooksys.ftd.week3.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	public static final int DB_RECONNECT_TIMEOUT = 3000;
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/week3_project";
	public static final String DB_USERNAME = "root";
	public static final String DB_PASSWORD = "bondstone";

	public static Connection connection;

	public static void setConnection(Connection c) {
		connection = c;
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
}
