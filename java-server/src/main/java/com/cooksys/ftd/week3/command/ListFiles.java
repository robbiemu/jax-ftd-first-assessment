package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import com.cooksys.ftd.week3.db.DBConnection;
import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.User;
import com.cooksys.ftd.week3.transactions.Credentials;
import com.cooksys.ftd.week3.transactions.Result;
import com.cooksys.ftd.week3.transactions.ServerMessage;

public class ListFiles implements AbstractCommand {

	private PrintWriter writer;
	private Map<String, Object> args;
	private Credentials credentials;

	private UserDao userDao = new UserDao();
	private FileDao fileDao = new FileDao();
	
	public ListFiles () {
		super();
	}
	
	@Override
	public ServerMessage executeCommand(Map<String, Object> args) {
		User user = new User();		
		String[] filenames = null;
		
		ServerMessage sm = new ServerMessage();
		try {
			user = userDao.getUserByUsername(credentials.getUsername(), DBConnection.connection);
			
			filenames = fileDao.getFilenamesByUserFK(user.getPrimaryKey(), DBConnection.connection);
			
		} catch (SQLException e) {
			sm.setError(true);
			sm.setMessage("Cannot retreive list of files for user " + user.getUsername() + 
					" Reason: " + e.getMessage());
		}
		
		if (!sm.getError()) {
			sm.setMessage("File list retreived for user " + user.getUsername());
			
			Result r = new Result();
			r.setResult("Files for user: " + user.getUsername() + "\n" + Arrays.stream(filenames).collect(Collectors.joining("\n")));
			sm.setData(r);

			try {
				JAXBContext jc = JAXBContext.newInstance(Result.class);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				
				StringWriter sw = new StringWriter();
				marshaller.marshal( r, new PrintWriter(sw) );
							
				sm.setMessage(sw.toString());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	@Override
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
