package org.bit.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bit.mail.Mail;

public class MysqlAccess extends Thread implements DictAccess,MailAccess{
	/** 
	 * Driver of Mysql is:com.mysql.jdbc.Driver
	 * */
	static Connection conn;
	final static private String DRIVER = "com.mysql.jdbc.Driver";
	final static private String SUM_NAME = "^S^U^M^";
	
	final static private int ERROR_CODE_RECORD_NOT_EXISTED = 1032;
	final static private int ERROR_CODE_DATABASE_NOT_EXISTED = 1049;
	final static private int ERROR_CODE_DATABASE_ALREADY_EXISTED = 1007;/*when creating database*/
	final static private int ERROR_CODE_TALBE_NOT_EXISTED = 1051;
	final static private int ERROR_CODE_TABLE_ALREADY_EXISTED = 1050;
	
	final static private String SPAM_DICT_TABLE_NAME = "Spam_Dictionary";
	final static private String NORMAL_DICT_TABLE_NAME = "Normal_Dictionary";
	final static private String MAIL_TABLE_NAME = "Mail";
	
	private Statement statement;
	
	private String url;
	private String user;
	private String password;
	
	MysqlAccess(String url,String user,String password)
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
	public int createDatabase() {		
		Connection conn = getConnection();
		String createStatement = "CREATE DATABASE Spam_Data";
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("create database failed");
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * CREATE TABLE Spam_Dictionary(
	 * 		Dict_word TEXT UNIQUE NOT NULL,
	 * 		Dict_frequency INTEGER UNSIGNED NOT NULL;
	 * );
	 * 
	 * CREATE TABLE Normal_Dictionary(
	 * 		Dict_word TEXT UNIQUE NOT NULL,
	 * 		Dict_frequency INTEGER UNSIGNED NOT NULL;
	 * );
	 * 
	 * INSERT INTO Spam_Dictionary values("^S^U^M^",0);
	 * 
	 * INSERT INTO Normal_Dcitionary values("^S^U^M^",0);
	 * 
	 * the first item contains the sum frequency of all words
	 * */
	@Override
	public int createTableDict() {
		
		Connection conn = getConnection();
		String createSpamDictStatement = "CREATE TABLE " + SPAM_DICT_TABLE_NAME + " (" + 
				"Spam_word TEXT UNIQUE NOT NULL,"+
				"Spam_frequency INTEGER NOT NULL" + ")";
		String createNormalDictStatement = "CREATE TABLE " + NORMAL_DICT_TABLE_NAME + " (" +
				"Normal_word TEXT UNIQUE NOT NULL,"+
				"Normal_frequency INTEGER NOT NULL" + ")";
		String addSumRecordInSpamDict = "INSERT INTO" + SPAM_DICT_TABLE_NAME + "values(" + SUM_NAME + " , 0)";
		String addSumRecordInNormalDict = "INSERT INTO" + NORMAL_DICT_TABLE_NAME + "values(" + SUM_NAME + " , 0)";;
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createSpamDictStatement);
			statement.executeUpdate(createNormalDictStatement);
			statement.executeUpdate(addSumRecordInNormalDict);//I don't need to use insert()
			statement.executeUpdate(addSumRecordInSpamDict);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("create table dict failed");
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * CREATE TABLE Mail(
	 * 		Mail_ID INTERGER PRIMARY KEY,
	 * 		Mail_content TEXT,
	 * 		Mail_tag BOOL,
	 * 		Mail_author TEXT,
	 * 		Mail_subject TEXT,
	 * 		Mail_date DATE
	 * );
	 * */
	@Override
	public int createTableMail() {		
		Connection conn = getConnection();
		String createStatement = "CREATE TABLE " + MAIL_TABLE_NAME +" (" + 
				"Mail_ID INTERGER PRIMARY KEY,"+
				"Mail_content TEXT," + 
				"Mail_tag BOOL," + //if the mail is spam
				"Mail_author TEXT," + 
				"Mail_subject TEXT," + 
				"Mail_date DATE" + ")";
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("create table mail failed");
			e.printStackTrace();
		}
		return 0;
	}

	
	/**
	 * 
	 * SELECT Dict_frequency WHERE Dict_word == $word FROM $TABLENAME;
	 * 
	 * Query frequency of each words from Dict database 
	 * @param tag query from `spam_dict` or `normal_dict` ,true stands for `spam_dict`
	 * @param isInsert whether to insert a new word if not exists in database
	 * @param wordlist words prepared to query
	 * @return a hashmap of <word,frequency>,and includes SUM item 
	 * */
	@Override
	public HashMap<String, Integer> query(boolean tag, boolean isInsert, List<String> wordlist) {
		Connection conn = getConnection();
		Iterator<String> worditer = wordlist.iterator();
		String tableName = tag == true? SPAM_DICT_TABLE_NAME : NORMAL_DICT_TABLE_NAME;
		ResultSet rs = null;
		String thisWord = null;
		HashMap<String,Integer> hashResult = null;
		
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery("SELECT Dict_frequency WHERE Dict_word == " + SUM_NAME + " FROM " + tableName);
			hashResult.put(SUM_NAME, rs.getInt(2));
		} catch (SQLException e1) {
			System.out.println("Get sum error");
			e1.printStackTrace();
		}
				
		while(worditer.hasNext())
		{
			thisWord = worditer.next();
			try {
				rs = statement.executeQuery("SELECT Dict_frequency WHERE Dict_word == " + thisWord + " FROM " + tableName);
				//FIXME index here is not standard
				hashResult.put(thisWord, rs.getInt(2));
				
			} catch (SQLException e) {
				if(e.getErrorCode() == ERROR_CODE_RECORD_NOT_EXISTED)//if no this word
				{
					System.out.printf("no word : " + thisWord);
					if(isInsert)
						insert(tag, thisWord);
				}
				e.printStackTrace();
			}
		}
		
		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("statment or conn close failed when query");
			e.printStackTrace();
		}
		return hashResult;
	}

	/**
	 * INSERT INTO $tableName (Dict_word,Dict_frequency) VALUES ('$word',1)
	 * 		ON DUPLICATE KEY UPDATE Dict_frequecy = Dict_frquency + 1;
	 * 
	 * It's lucky that MySQL supports duplication detectation.If the word exists,Dict_frequency ++;if not, create a new record.
	 * It works automatically!
	 * @param tag insert into `spam_dict` or `normal_dict` ,true stands for `spam_dict`
	 * */
	private int insert(boolean tag,String word) {
		String tableName = tag == true ? SPAM_DICT_TABLE_NAME : NORMAL_DICT_TABLE_NAME;
		String insertStatement = "INSERT INTO " + tableName + " (Dict_word, Dict_frequency) VALUES ('" +  word + "', 1)"
				+ "ON DUPLICATE KEY UPDATE Dict_frequency = Dict_frequency + 1";
		String addSumRecord = "UPDATE " + tableName + "SET Dict_frequency = Dict_frequency + 1 " + "where Dict_word == " + SUM_NAME;//sum ++
		
		try {
			statement.executeUpdate(insertStatement);
			statement.executeUpdate(addSumRecord);
		} catch (SQLException e) {
			System.out.println(word + "Insert " + word + " Failed");
			e.printStackTrace();
			return 1;
		}
		
		return 0;
	}

	/**
	 * Insert a list of words.
	 * */
	@Override
	public int insert(boolean tag,List<String> word)
	{
		Iterator<String> worditer = word.iterator();
		conn = getConnection();
		try {
			statement = conn.createStatement();
			while(worditer.hasNext())
			{
				insert(tag,worditer.next());
			}
		} catch (SQLException e) {
			System.out.println("create statement failed when insert a list of words");
			e.printStackTrace();
			return 1;
		}
		
		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("close failed when insert a list of words");
			e.printStackTrace();
		}

		return 0;
	}
	
	/**
	 * INSERT INTO $tableName (Dict_word,Dict_frequency) VALUES ('$word',0)
	 * 		ON DUPLICATE KEY UPDATE Dict_frequecy = Dict_frquency - 1; 
	 * 
	 * It's impossible to delete a word directly.
	 * This method only supports "frequency --"
	 * "delete" means "delete all words in a certain mail from database"
	 * */
	private int delete(boolean tag,String word) {
		String tableName = tag == true ? SPAM_DICT_TABLE_NAME : NORMAL_DICT_TABLE_NAME;
		String deleteStatement = "INSERT INTO " + tableName + " (Dict_word, Dict_frequency) VALUES ('" + word + "', 0)"
		  		+ "ON DUPLICATE KEY UPDATE Dict_frequency = Dict_frquency - 1";
		try {
			statement = conn.createStatement();
			statement.execute(deleteStatement);
		} catch (SQLException e) {
			System.out.println("fail to delete " + word);
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	@Override
	public int delete(boolean tag,List<String> word)
	{
		Iterator<String> worditer = word.iterator();
		conn = getConnection();
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			System.out.println("statement create failed when delete a list of words");
			e.printStackTrace();
		}
		while(worditer.hasNext())
		{
			delete(tag,worditer.next());
		}
		
		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Statment or conn close failed when delete a list of words");
			e.printStackTrace();
		}
		return 0;
	}

	
	/**
	 * Mail database 
	 * */
	@Override
	public int delete(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modify(Mail mail) {
		// TODO Auto-generated method stub
		return 0;
	}



	
}
