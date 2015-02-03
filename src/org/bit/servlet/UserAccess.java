package org.bit.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bit.conn.MysqlAccess;

/**
 * FIXME there is defect in class-designing.
 * I'm support to implement 'DatabaseAccess' to create a new interface for a certain table in database.
 * But now,I'm not able...
 * * */
public class UserAccess{
	String url;
	String user;
	String password;
	public final static String USER_TABLE_NAME = "Privilege";
	
	UserAccess(String url,String user,String password){
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	boolean check(String checkUser,String checkPassword) throws SQLException
	{
		MysqlAccess conn = new MysqlAccess(this.url, this.user, this.password);
		String statement = "SELECT * FROM " + USER_TABLE_NAME + " WHERE (user = '" + checkUser  + "' && password = '" + checkPassword + "')";
		ResultSet rs = conn.query(statement);
		if(rs.next())
			return true;
		else
			return false;

	}


	


}
