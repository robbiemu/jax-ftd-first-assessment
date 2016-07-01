package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jdt.annotation.NonNull;

import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.User;
import com.cooksys.ftd.week3.transactions.Credentials;
import com.cooksys.ftd.week3.transactions.ServerMessage;

@XmlRootElement
public class RegisterUser implements AbstractCommand {
	private PrintWriter writer;
	private Map<String, Object> args;
	private Credentials credentials;
	
	private UserDao userDao = new UserDao();
	
	public RegisterUser(){
		super();
	}
	
	@Override
	public ServerMessage executeCommand(@NonNull Map<String, Object> args) {
		User user = new User();
		user.setUsername((String)args.get(User.USERNAME_COLUMN));
		user.setPassword((String)args.get(User.PASSWORD_COLUMN));
		
		ServerMessage sm = new ServerMessage();
		try {
			userDao.insertUser(user, DBConnection.connection);
		} catch (SQLException sqle) {
			sm.setError(true);
			sm.setMessage("Cannot register credentials for user " + user.getUsername() + 
					" Reason: " + sqle.getMessage());
		}

		if (!sm.getError()) {
			sm.setMessage("Credentials registered for user " + user.getUsername());
		}

		return sm;
	}

	@Override
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	@Override
	public PrintWriter getWriter() {
		return this.writer;
	}

	@Override
	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}

	@Override
	public Map<String, Object> getArgs() {
		return this.args;
	}


	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	@Override
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
