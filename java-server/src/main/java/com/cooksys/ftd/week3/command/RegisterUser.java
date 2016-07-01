package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.User;
import com.cooksys.ftd.week3.transactions.ServerMessage;

@XmlRootElement
public class RegisterUser implements AbstractCommand {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(RegisterUser.class);

	private PrintWriter writer;
	private Map<String, Object> args;
	
	private UserDao userDao = new UserDao();
	private FileDao fileDao = new FileDao();
	
	public RegisterUser(){
		super();
	}
	
	@Override
	public ServerMessage executeCommand(@NonNull Map<String, Object> args) {
		User user = new User();
		user.setUsername((String)args.get(User.USERNAME_COLUMN));
		user.setPassword((String)args.get(User.PASSWORD_COLUMN));
		
		boolean inserted = userDao.insertUser(user, DBConnection.connection);
		
		ServerMessage sm = new ServerMessage();
		sm.setError(!inserted);
		if(!inserted) {
			sm.setMessage("Error inserting user credentials into database");
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
