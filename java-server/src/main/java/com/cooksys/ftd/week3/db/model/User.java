package com.cooksys.ftd.week3.db.model;

public class User extends AbstractModel {
	public static final String PRIMARY_KEY = "idusers";
	public static final String USERNAME_COLUMN = "username";
	public static final String PASSWORD_COLUMN = "password_hash";
	
	private String username;
	private String password;

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	String primaryKeyFieldName() {
		return PRIMARY_KEY;
	}

}
