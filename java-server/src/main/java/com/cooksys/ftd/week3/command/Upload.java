package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.util.Map;

import com.cooksys.ftd.week3.transactions.ServerMessage;

public class Upload implements AbstractCommand {

	private PrintWriter writer;
	private Map<String, Object> args;

	@Override
	public ServerMessage executeCommand(Map<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
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

}
