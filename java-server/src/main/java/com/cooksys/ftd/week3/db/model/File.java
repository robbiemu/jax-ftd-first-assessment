package com.cooksys.ftd.week3.db.model;

public class File extends AbstractModel {
	public static final String PRIMARY_KEY = "idfiles";
	public static final String FILE_COLUMN = "file";
	public static final String FILENAME_COLUMN = "filename";
	public static final String FILES_USER_FK = "user_id";
	
	private String filename;
	private byte[] file;
	private Long files_user_fk;

	public File() {
	}

	public File(String filename, byte[] file) {
		this.filename = filename;
		this.file = file;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	String primaryKeyFieldName() {
		return PRIMARY_KEY;
	}

	public Long getFiles_user_fk() {
		return files_user_fk;
	}

	public void setFiles_user_fk(Long files_user_fk) {
		this.files_user_fk = files_user_fk;
	}

}
