package com.cooksys.ftd.week3.command;

import java.io.PrintWriter;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.ftd.week3.transactions.ServerMessage;

@XmlRootElement
public interface AbstractCommand extends Runnable {
	ServerMessage executeCommand(Map<String, Object> args);
	
	@Override
	default void run() {
		postResponse(executeCommand(getArgs()));
	}

	default void postResponse(ServerMessage response){
		if(response.hasError()) {
			// handle error state
		}
		getWriter().println(response.getMessage());
		getWriter().flush();
	}
	void setWriter(PrintWriter writer);
	PrintWriter getWriter();
	void setArgs(Map<String, Object> args);
	Map<String, Object> getArgs();
}