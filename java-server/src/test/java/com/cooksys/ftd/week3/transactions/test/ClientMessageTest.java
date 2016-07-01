package com.cooksys.ftd.week3.transactions.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cooksys.ftd.week3.App;
import com.cooksys.ftd.week3.command.AbstractCommand;
import com.cooksys.ftd.week3.command.CommandException;
import com.cooksys.ftd.week3.command.CommandRegistry;
import com.cooksys.ftd.week3.command.RegisterUser;
import com.cooksys.ftd.week3.command.TransmitError;
import com.cooksys.ftd.week3.transactions.ClientMessage;
import com.cooksys.ftd.week3.transactions.Credentials;
import com.cooksys.ftd.week3.transactions.ServerMessage;


public class ClientMessageTest {
	static PrintWriter writer;

	@BeforeClass
	public static void prep(){
		writer = new PrintWriter(System.out);
	}
	
	@AfterClass
	public static void teardown() {
	}
	
	@Test
	public void toJson() {
		Map<String, Object> args = new HashMap<>();
		args.put("stringfield", "value");
		args.put("numericfield", 1D);
		Credentials cred = new Credentials();
		cred.setUsername("meme");
		cred.setPassword("mypass");
		
		try {
			ClientMessage cm = new ClientMessage("RegisterUser", cred, args);

			JAXBContext jc = JAXBContext.newInstance(ClientMessage.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
			
			marshaller.marshal(cm, writer);
		} catch (JAXBException e) {
			fail("failed to marshall ClientMessage. Error: " + e.getMessage());
		} catch (InstantiationException | IllegalAccessException e) {
			fail("failed to instantiate ClientMessage. Error: " + e.getMessage());
		}
	}
	
	@Test
	public void fromJson() {
		String rawJson = "{\"clientMessage\":{\"commandClassName\":\"RegisterUser\",\"credentials\":{},\"args\":{\"entry\":[{\"key\":\"username\",\"value\":{\"type\":\"string\",\"value\":\"una\"}},{\"key\":\"password\",\"value\":{\"type\":\"string\",\"value\":\"pas\"}}]}}}";
		ClientMessage response = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(ClientMessage.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
			
	        StreamSource json = new StreamSource(new StringReader( rawJson ));
	        response = unmarshaller.unmarshal(json, ClientMessage.class).getValue();
		} catch (JAXBException e) {
			fail("failed to unmarshall ClientMessage. Error: " + e.getMessage());
		}
		assertFalse("failed to get a ClientMessage back", response == null);
						
		try {
			RegisterUser c = (RegisterUser) response.getCommand(writer);
			assertTrue("got a null command back from getCommand", c != null);
			
			assertTrue("Writer was left null", c.getWriter() != null);
			assertTrue("Writer not set", c.getWriter().equals(writer));
						
			assertTrue("args not set in command object generated from ClientMessage", 
					c.getArgs() != null);
			assertTrue("args object does not contain original key", c.getArgs().containsKey("password"));
			assertTrue("args object does not contain original key->value", c.getArgs().get("password").equals("pas"));
		} catch(Exception e) {
			fail("failed to getCommand from generated ClientMessage, error: " + e.getMessage());
		}
	}
}
