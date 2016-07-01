package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.transactions.Credentials;
import com.cooksys.ftd.week3.transactions.Errors;
import com.cooksys.ftd.week3.transactions.ServerMessage;

@XmlRootElement
public interface AbstractCommand extends Runnable {
	static Logger log = LoggerFactory.getLogger(AbstractCommand.class);

	ServerMessage executeCommand(Map<String, Object> args);
	
	@Override
	default void run() {
		postResponse(executeCommand(getArgs()));
	}

	default void postResponse(ServerMessage response){
		String message = response.getMessage();
		if(response.hasError()) {
			log.debug("command encountered an error. Error msg (if set): " + response.getMessage());

			Errors e = new Errors();
			e.setMessage(response.getMessage());
			e.setType(this.getClass().getSimpleName());

			try {
				JAXBContext jc = JAXBContext.newInstance(Errors.class);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
				
				StringWriter sw = new StringWriter();
				marshaller.marshal( e, new PrintWriter(sw) );
							
				message = sw.toString();
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		log.debug("sending: " + message);
		
		getWriter().print(message);
		getWriter().flush();
	}
	
	void setWriter(PrintWriter writer);
	PrintWriter getWriter();
	void setArgs(Map<String, Object> args);
	Map<String, Object> getArgs();

	void setCredentials(Credentials credentials);
}