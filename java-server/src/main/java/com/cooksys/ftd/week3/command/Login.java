package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.persistence.jaxb.MarshallerProperties;

import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.User;
import com.cooksys.ftd.week3.transactions.ClientMessage;
import com.cooksys.ftd.week3.transactions.Credentials;
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
		User user;
		ServerMessage sm = new ServerMessage();
		try {
			user = userDao.getUserByUsername(
					(String)args.get(User.USERNAME_COLUMN), DBConnection.connection);
		} catch (SQLException e1) {
			sm.setError(true);
			sm.setMessage(e1.getMessage());
			if(!e1.getMessage().equals("Illegal operation on empty result set.")) {
				e1.printStackTrace();
			}
			return sm;
		}
		
		Credentials c = new Credentials();
		c.setPassword(user.getPassword());
		sm.setData(c);
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Credentials.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
			
			StringWriter sw = new StringWriter();
			marshaller.marshal( c, new PrintWriter(sw) );
						
			sm.setMessage(sw.toString());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
