package com.cooksys.ftd.week3.db.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.ftd.week3.db.model.User;
import static com.cooksys.ftd.week3.db.model.User.*;

public class UserDao extends Dao {
	private static Logger log = LoggerFactory.getLogger(UserDao.class);

	public static final String EUSER_OR_PWD = "error with username or password";

	public void insertUser(User user, Connection c) throws SQLException {
		String sql = "INSERT INTO users ("+USERNAME_COLUMN+", "+PASSWORD_COLUMN+") VALUES ('" + 
					user.getUsername() + "', '" + user.getPassword() + "')";
		
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			int rowsInserted = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			log.warn("Error preparing or executing sql: " + sql);
			log.warn(e.getMessage());
			if(!e.getMessage().substring(0, 15).equals("Duplicate entry")) {
				e.printStackTrace();				
			}
			throw(e);
		}
	}
	
	public User getUserByUsername(String username, Connection c) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = '" + username + "'";

		User user = null;
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Long pk = rs.getLong(User.PRIMARY_KEY);
			String password = rs.getString(User.PASSWORD_COLUMN);
			
			user = new User(username, password);
			user.setPrimaryKey(pk);
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			log.warn("Error preparing or executing sql: " + sql);
			log.warn(e.getMessage());
			if(!e.getMessage().equals("Illegal operation on empty result set.")) {
				e.printStackTrace();
			}
			throw(e);
		}
		return user;
	}
}
