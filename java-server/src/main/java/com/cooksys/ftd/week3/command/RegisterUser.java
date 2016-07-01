package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.transactions.ServerMessage;

@XmlRootElement
public class RegisterUser implements AbstractCommand {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(RegisterUser.class);

	private PrintWriter writer;
	private Map<String, Object> args;
	
	public RegisterUser(){
		super();
	}
	
	@Override
	public ServerMessage executeCommand(@NonNull Map<String, Object> args) {
		Server
		return null;
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
}
