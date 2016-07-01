package com.cooksys.ftd.week3.dao.test;

import static com.cooksys.ftd.week3.db.DBConnection.DB_PASSWORD;
import static com.cooksys.ftd.week3.db.DBConnection.DB_USERNAME;
import static com.cooksys.ftd.week3.db.DBConnection.DRIVER;
import static com.cooksys.ftd.week3.db.DBConnection.URL;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cooksys.ftd.week3.db.dao.FileDao;
import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.File;
import com.cooksys.ftd.week3.db.model.User;

public class FileDaoTest {
	static Connection connection;
	
	@BeforeClass
	public static void prep(){
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void teardown(){
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void dbio(){
		UserDao ud = new UserDao();
		User u = new User();
		u.setPassword("y");
		u.setUsername("me");
		ud.insertUser(u, connection);
		u = ud.getUserByUsername("me", connection);
		
		FileDao fd = new FileDao();
		File file = new File();
		byte[] fileBytes = "abc123".getBytes();
		
		file.setFile(fileBytes);
		file.setFilename("testfile");
		file.setFiles_user_fk(u.getPrimaryKey());

		fd.insertFile(file, connection);

		fd = new FileDao();
		File testf = fd.getFileByFilename("testfile", connection);
		String string_of_bytes = new String(testf.getFile());
		assertTrue("File data: " + string_of_bytes + " is not 'abc123'", string_of_bytes.equals("abc123"));
		
		System.out.println("FileDaoTest - dbio - passed");
		
		String sqlf = "DELETE FROM files WHERE "+ File.PRIMARY_KEY +" = " + testf.getPrimaryKey();
		String sqlu = "DELETE FROM users WHERE "+ User.PRIMARY_KEY +" = " + u.getPrimaryKey();
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sqlf);
			ps.executeUpdate();
			ps = connection.prepareStatement(sqlu);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}
}
