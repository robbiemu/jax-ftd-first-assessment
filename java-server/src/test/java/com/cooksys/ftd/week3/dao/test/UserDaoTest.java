package com.cooksys.ftd.week3.dao.test;

import static com.cooksys.ftd.week3.db.DBConnection.DB_PASSWORD;
import static com.cooksys.ftd.week3.db.DBConnection.DB_USERNAME;
import static com.cooksys.ftd.week3.db.DBConnection.DRIVER;
import static com.cooksys.ftd.week3.db.DBConnection.URL;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cooksys.ftd.week3.db.dao.UserDao;
import com.cooksys.ftd.week3.db.model.User;

public class UserDaoTest {
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
		User user = new User();
		
		user.setUsername("testuser");
		user.setPassword("testpass");
		
		ud.insertUser(user, connection);
		
		ud = new UserDao();
		User testf = ud.getUserByUsername("testuser", connection);
		assertTrue("Password: '" + testf.getPassword() + "' is not 'testpass'", 
				testf.getPassword().equals("testpass"));
		
		System.out.println("UserDaoTest - dbio - passed");
		
		String sql = "DELETE FROM users WHERE "+ User.PRIMARY_KEY +" = " + testf.getPrimaryKey();
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
