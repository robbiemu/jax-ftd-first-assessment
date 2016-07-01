package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.File;
import com.cooksys.ftd.week3.db.model.User;
import com.cooksys.ftd.week3.transactions.Credentials;
import com.cooksys.ftd.week3.transactions.ServerMessage;

public class Upload implements AbstractCommand {
	static Logger log = LoggerFactory.getLogger(Upload.class);

	private PrintWriter writer;
	private Map<String, Object> args;
	private Credentials credentials;

	private UserDao userDao = new UserDao();
	private FileDao fileDao = new FileDao();
	
	public Upload(){
		super();
	}
	
	@Override
	public ServerMessage executeCommand(@NonNull Map<String, Object> args) {
		User user = new User();
						
		File file = new File();
		String b64f = (String) args.get("file");
		file.setFile(b64f.getBytes());
		file.setFilename((String) args.get("filepath"));
		
		ServerMessage sm = new ServerMessage();
		try {
			user = userDao.getUserByUsername(credentials.getUsername(), DBConnection.connection);
			file.setFiles_user_fk(user.getPrimaryKey());
			
			fileDao.insertFile(file, DBConnection.connection);			
		} catch (SQLException e) {
			sm.setError(true);
			sm.setMessage("Cannot register credentials for user " + user.getUsername() + 
					" Reason: " + e.getMessage());
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
		return writer;
	}

	@Override
	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}

	@Override
	public Map<String, Object> getArgs() {
		return args;
	}


	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	public FileDao getFileDao() {
		return fileDao;
	}


	public void setFileDao(FileDao fileDao) {
		this.fileDao = fileDao;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	@Override
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
