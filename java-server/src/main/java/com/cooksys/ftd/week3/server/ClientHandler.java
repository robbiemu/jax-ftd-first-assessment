package com.cooksys.ftd.week3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.App;
import com.cooksys.ftd.week3.command.AbstractCommand;
import com.cooksys.ftd.week3.command.TransmitError;
import com.cooksys.ftd.week3.transactions.ClientMessage;
import com.cooksys.ftd.week3.transactions.ServerMessage;

public class ClientHandler implements Runnable {
	private Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
		
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		String rawClientInput;
		try {
			while (!socket.isClosed()) {
				rawClientInput = reader.readLine();
				
				if (rawClientInput == null) {
					try {
						Thread.currentThread().join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				log.debug(rawClientInput); // TODO - remove this debug
				
				ClientMessage clientResponse = interpretInput(rawClientInput);
				
				AbstractCommand ac = clientResponse.getCommand(writer);
				
//				log.debug("class of abstractcommand: " + ac.getClass());
//				log.debug("args: " + ac.getArgs());
				
				ac.setWriter(writer);
				App.executor.execute(ac);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ClientMessage interpretInput(String rawClientInput) {
		ClientMessage response = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(ClientMessage.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
			
	        StreamSource json = new StreamSource(new StringReader( rawClientInput ));
	        response = unmarshaller.unmarshal(json, ClientMessage.class).getValue();
		} catch (JAXBException e) {
			ServerMessage s = new ServerMessage();
			s.setError(true);
			s.setMessage("invalid client message: could not generate ClientMessage instance from JSON");

			TransmitError t = new TransmitError();
			t.setWriter(writer);
			t.setServerResponse(s);
			App.executor.execute(t);
			
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	public BufferedReader getReader() {
		return reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

}
