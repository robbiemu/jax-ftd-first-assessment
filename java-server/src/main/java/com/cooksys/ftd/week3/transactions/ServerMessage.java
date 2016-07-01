package com.cooksys.ftd.week3.transactions;

public class ServerMessage {
	private Boolean error = false; // true on error yielding no data
	private String message; // optional message field
	private Object data; // optional database object reference
	
	public ServerMessage() {
	}
	
	public ServerMessage(Object data) {
		this.data = data;
	}	

	public ServerMessage(Boolean error, String message, Object data) {
		this.error = error;
		this.message = message;
		this.data = data;
	}

	public Boolean hasError() {
		return error;
	}
	
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
