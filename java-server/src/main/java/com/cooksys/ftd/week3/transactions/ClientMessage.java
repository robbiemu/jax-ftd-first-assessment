package com.cooksys.ftd.week3.transactions;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.ftd.week3.command.AbstractCommand;
import com.cooksys.ftd.week3.command.CommandException;
import com.cooksys.ftd.week3.command.CommandRegistry;

@XmlRootElement
public class ClientMessage {
	public static final String USERNAME = "username";	
	public static final String PASSWORD = "password";
	
	private String commandClassName;
	private Credentials credentials;
	private Map<String, Object> args = new HashMap<>();
	
	public ClientMessage() {
		credentials = new Credentials();
	}
	public ClientMessage(String cc, Credentials credentials, Map<String, Object> args) 
			throws InstantiationException, IllegalAccessException {
		this.commandClassName = cc;
		this.credentials = credentials;
		this.args = args;
	}

	public AbstractCommand getCommand(PrintWriter writer) {
		AbstractCommand ac = null;
		try {
			Class<? extends AbstractCommand> commandClass = getCommandClass();
			ac = (AbstractCommand) commandClass.newInstance();
			ac.setWriter(writer);
			ac.setArgs(args);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommandException e) {
			// TODO - raise a ServerResponse error - command not a valid command
		}
		return ac;
	}

	private Class<? extends AbstractCommand> getCommandClass() throws CommandException {
		if (CommandRegistry.registry.containsKey(commandClassName)) {
			return CommandRegistry.registry.get(commandClassName);
		} else {
			throw new CommandException("command issued by client is not a command object");
		}
	}

	public String getCommandClassName() {
		return commandClassName;
	}

	public void setCommandClassName(String commandClassName) {
		this.commandClassName = commandClassName;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public Map<String, Object> getArgs() {
		return args;
	}

	public void setArgs(Map<String, Object> args) {
		this.args = args;
	}
}
