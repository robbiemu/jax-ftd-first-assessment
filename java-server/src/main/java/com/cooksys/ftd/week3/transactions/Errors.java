package com.cooksys.ftd.week3.transactions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Errors {
	public String type;
	public String message;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	
}
