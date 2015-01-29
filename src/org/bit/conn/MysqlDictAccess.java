package org.bit.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import org.bit.mail.Mail;

public class MysqlDictAccess implements DictAccess,MailAccess{
	/** 
	 * Driver of Mysql is:com.mysql.jdbc.Driver
	 * */
	static Connection conn;
	final static private String DRIVER = "com.mysql.jdbc.Driver";
	private Statement statement;
	
	private String url;
	private String user;
	private String password;
	
	MysqlDictAccess(String url,String user,String password)
	{
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	
	public Connection getConnection()
	{
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Database connection success");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Datavase connection failure");
		}
		return conn;
	}
	
	
	/**
	 * CREATE DATABASE Spam_Data
	 * */
	@Override
	public int CreateDatabase() {		
		Connection conn = getConnection();
		String createStatement = "CREATE DATABASE Spam_Data";
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		return 0;
	}

	
	/**
	 * CREATE TABLE Spam_Dictionary(
	 * 		Dict_word TEXT,
	 * 		Dict_frequency INTEGER;
	 * );
	 * 
	 * CREATE TABLE Normal_Dictionary(
	 * 		Dict_word TEXT,
	 * 		Dict_frequency INTEGER;
	 * );
	 * the first item contains the sum frequency of all words
	 * */
	@Override
	public int CreateTableDict() {
		
		Connection conn = getConnection();
		String createSpamDictStatement = "CREATE TABLE Spam_Dictionary (" + 
				"Spam_word TEXT NOT NULL,"+
				"Spam_frequency INTEGER NOT NULL" + ")";
		String createNormalDictStatement = "CREATE TABLE Normal_Dictionary (" +
				"Normal_word TEXT NOT NULL,"+
				"Normal_frequency INTEGER NOT NULL" + ")";
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createSpamDictStatement);
			statement.executeUpdate(createNormalDictStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * CREATE TABLE Mail(
	 * 		Mail_ID INTERGER,
	 * 		Mail_Content TEXT,
	 * 		Mail_Tag BOOL,
	 * 		Mail_Author TEXT,
	 * 		Mail_Subject TEXT,
	 * 		Mail_Date DATE
	 * );
	 * */
	@Override
	public int CreateTableMail() {		
		Connection conn = getConnection();
		String createStatement = "CREATE TABLE Mail (" + 
				"Mail_ID INTERGER,"+
				"Mail_Content TEXT," + 
				"Mail_Tag BOOL," + //if the mail is spam
				"Mail_Author TEXT," + 
				"Mail_Subject TEXT," + 
				"Mail_Date DATE" + ")";
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	/**
	 * Dict database
	 * */
	@Override
	public HashMap<String, Integer> Query(boolean tag,List<String> wordlist) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int Insert(boolean tag,String word) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int Modify(boolean tag,String word) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public int Delete(boolean tag,String word) {
		// TODO Auto-generated method stub
		return 0;
	}


	
	/**
	 * Mail database 
	 * */
	@Override
	public int Delete(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int Insert(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int Modify(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}


	
}
