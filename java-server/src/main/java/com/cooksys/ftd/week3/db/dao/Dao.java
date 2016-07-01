package com.cooksys.ftd.week3.db.dao;

import java.sql.Connection;

public class Dao {
	public static final String EDB = "error with database";

	private Connection conn;

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
