package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.User;
import com.cooksys.ftd.week3.transactions.ServerMessage;

public class Login implements AbstractCommand {

	private PrintWriter writer;
	private Map<String, Object> args;

	private UserDao userDao = new UserDao();
	private FileDao fileDao = new FileDao();
	
	public Login(){
		super();
	}
	
	@Override
	public ServerMessage executeCommand(@NonNull Map<String, Object> args) {
/* UserDao throws an error on getUserByUsername having no results. It also ignores
 *	further results, because the database schema has unique usernames. ServerMessage
 * will always be a success message with the password_hash */
		User user = userDao.getUserByUsername(
				(String)args.get(User.USERNAME_COLUMN), DBConnection.connection);
		
		ServerMessage sm = new ServerMessage();
		sm.setMessage(user.getPassword());
		
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

}
