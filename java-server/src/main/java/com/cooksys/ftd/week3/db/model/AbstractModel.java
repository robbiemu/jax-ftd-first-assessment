package com.cooksys.ftd.week3.db.model;

public abstract class AbstractModel {
	private Long primaryKey;
	
	abstract String primaryKeyFieldName();

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
}
