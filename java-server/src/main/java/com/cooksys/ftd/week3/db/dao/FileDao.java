package com.cooksys.ftd.week3.db.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.db.model.File;

public class FileDao extends Dao {
	private static Logger log = LoggerFactory.getLogger(FileDao.class);

	public boolean insertFile(File file, Connection c) {
		String sql = "INSERT INTO files ("+File.FILENAME_COLUMN+", "+File.FILES_USER_FK+", "+File.FILE_COLUMN+") VALUES (?,?,?)";
	
		try {			
			PreparedStatement ps = c.prepareStatement(sql);
			
			ps.setString(1, file.getFilename());
			ps.setLong(2, file.getFiles_user_fk());
			ps.setBinaryStream(3, new ByteArrayInputStream(file.getFile()), file.getFile().length);
	
			int rowsInserted = ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			log.warn("Error preparing or executing sql: " + sql);
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;	
	}
	
	public File getFileByFilename(String fqn, Connection c) {
		String sql = "SELECT * FROM files WHERE filename = '" + fqn + "'";

		File file = null;
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Long pk = rs.getLong(File.PRIMARY_KEY);
			byte[] fileData = rs.getBytes(File.FILE_COLUMN);
			
			file = new File(fqn, fileData);
			file.setPrimaryKey(pk);
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			log.warn("Error preparing or executing sql: " + sql);
			log.warn(e.getMessage());
			e.printStackTrace();
		}
		return file;
	}
}
