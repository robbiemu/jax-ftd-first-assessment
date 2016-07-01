package com.cooksys.ftd.week3.transactions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
	String result;
	
	public Result(){
		
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
