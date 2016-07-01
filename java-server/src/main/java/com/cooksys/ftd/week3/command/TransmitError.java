package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.util.Map;

import com.cooksys.ftd.week3.transactions.Credentials;
import com.cooksys.ftd.week3.transactions.ServerMessage;

public class TransmitError implements AbstractCommand {
	ServerMessage serverMessage;
	private PrintWriter writer;
	private Map<String, Object> args;
	private Credentials credentials;
	
	@Override
	public ServerMessage executeCommand(Map<String, Object> args) {
		return serverMessage;
	}

	public void setServerResponse(ServerMessage s) {
		this.serverMessage = s;
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
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public Credentials getCredentials() {
		return credentials;
	}
	
	@Override
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
