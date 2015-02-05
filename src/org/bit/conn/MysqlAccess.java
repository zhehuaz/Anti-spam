package org.bit.conn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.bit.mail.Email;
import org.bit.mail.Mail;

public class MysqlAccess implements DictAccess,MailAccess{
	/** 
	 * Driver of Mysql is:com.mysql.jdbc.Driver
	 * */
	static Connection conn;
	final static private String DRIVER = "com.mysql.jdbc.Driver";
	final static public String SUM_NAME = "^S^U^M^";
	
	final static private int ERROR_CODE_RECORD_NOT_EXISTED = 1032;
	final static private int ERROR_CODE_DATABASE_NOT_EXISTED = 1049;
	final static private int ERROR_CODE_DATABASE_ALREADY_EXISTED = 1007;/*when creating database*/
	final static private int ERROR_CODE_TALBE_NOT_EXISTED = 1051;
	final static private int ERROR_CODE_TABLE_ALREADY_EXISTED = 1050;
	
	final static private String SPAM_DICT_TABLE_NAME = "Spam_Dictionary";
	final static private String NORMAL_DICT_TABLE_NAME = "Normal_Dictionary";
	final static private String MAIL_TABLE_NAME = "Mail";
	
	final static private boolean debugMode = false;
	
	private Statement statement;
	
	private String url;
	private String user;
	private String password;
	
	public MysqlAccess(String propFilePath) throws FileNotFoundException, IOException, SQLException{
		Properties prop = new Properties();
		prop.load(new FileInputStream(propFilePath));

		this.url = prop.getProperty("url");
		this.user = prop.getProperty("user");
		this.password = prop.getProperty("password");
		if(getConnection() == null)
			throw new SQLException();
	}
	
	public MysqlAccess(String url,String user,String password) throws SQLException{
		this.url = url;
		this.user = user;
		this.password = password;
		if(getConnection() == null)
			throw new SQLException();
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
			if(debugMode)
				System.out.println("Database connection success");	
		} catch (SQLException e) {
			e.printStackTrace();
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
		if(debugMode)
			System.out.println(createStatement);
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
	 * CREATE TABLE IF NOT EXISTS $SpamTableName(
	 * 		Dict_id INT(10) AUTO_INCREMENT,
	 * 		Dict_word VARCHAR(50) UNIQUE NOT NULL,
	 * 		Dict_frequency INT(15) UNSIGNED NOT NULL,
	 * 		PRIMARY	KEY(Dict_id)
	 * )DEFAULT CHARSET=utf8;
	 * 
	 * CREATE TABLE IF NOT EXISTS $NormalTableName(
	 * 		Dict_id INT(10) AUTO_INCREMENT,
	 * 		Dict_word VARCHAR(50) UNIQUE NOT NULL,
	 * 		Dict_frequency INT(15) UNSIGNED NOT NULL,
	 * 		PRIMARY	KEY(Dict_id)
	 * )DEFAULT CHARSET=utf8;
	 * 
	 * INSERT INTO $SpamTableName (Dict_word, Dict_frequency) values($sumName, 0);
	 * INSERT INTO $NormalTableName (Dict_word, Dict_frequency) values($sumName, 0);
	 * 
	 * the first item contains the sum frequency of all words
	 * */
	@Override
	public int createTableDict() {
		
		Connection conn = getConnection();
		String createSpamDictStatement = "CREATE TABLE IF NOT EXISTS " + SPAM_DICT_TABLE_NAME
				 	+	" ( Dict_id INT(10) AUTO_INCREMENT,"
				 	+	"Dict_word VARCHAR(50) UNIQUE NOT NULL,"
				 	+	"Dict_frequency INT(15) UNSIGNED NOT NULL,"
				 	+	"PRIMARY KEY(Dict_id)"
				 	+ 	" )DEFAULT CHARSET=utf8";
		String createNormalDictStatement = "CREATE TABLE IF NOT EXISTS " + NORMAL_DICT_TABLE_NAME
			 	+	" ( Dict_id INT(10) AUTO_INCREMENT,"
			 	+	"Dict_word VARCHAR(50) UNIQUE NOT NULL,"
			 	+	"Dict_frequency INT(15) UNSIGNED NOT NULL,"
			 	+	"PRIMARY KEY(Dict_id)"
			 	+ 	" )DEFAULT CHARSET=utf8";
		String 	insertSumRecordInSpamDict = "INSERT INTO " + SPAM_DICT_TABLE_NAME + " (Dict_word, Dict_frequency) VALUES ('" +  SUM_NAME + "', 0)"
				+ "ON DUPLICATE KEY UPDATE Dict_frequency = Dict_frequency";
		String insertSumRecordInNormalDict = "INSERT INTO " + NORMAL_DICT_TABLE_NAME + " (Dict_word, Dict_frequency) VALUES ('" +  SUM_NAME + "', 0)"
				+ "ON DUPLICATE KEY UPDATE Dict_frequency = Dict_frequency";
		if(debugMode)
		{
			System.out.println(createNormalDictStatement);
			System.out.println(createSpamDictStatement);
			System.out.println(insertSumRecordInNormalDict);
			System.out.println(insertSumRecordInSpamDict);
		}
		try {
			statement = conn.createStatement();
			statement.executeUpdate(createSpamDictStatement);
			statement.executeUpdate(createNormalDictStatement);
			statement.executeUpdate(insertSumRecordInNormalDict);//I don't need to use insert()
			statement.executeUpdate(insertSumRecordInSpamDict);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * CREATE TABLE Mail(
	 * 		Mail_id INT(12) AUTO_INCREMENT,
	 * 		Mail_content TEXT,
	 * 		Mail_tag BOOL,
	 * 		PRIMARY KEY(Mail_id)
	 * )DEFAULT CHARSET=utf8;
	 * */
	@Override
	public int createTableMail() {		
		Connection conn = getConnection();
		String createStatement = "CREATE TABLE IF NOT EXISTS " + MAIL_TABLE_NAME +" (" + 
				"Mail_id INT(12) AUTO_INCREMENT,"+
				"Mail_content TEXT," + 
				"Mail_tag BOOL," + //if the mail is spam
				"PRIMARY KEY(Mail_id)" +
				 ")DEFAULT CHARSET=utf8";
		if(debugMode)
		{
			System.out.println(createStatement);
		}
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
	 * SELECT * WHERE Dict_word = $word FROM $TABLENAME;
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
		wordlist.add(SUM_NAME);
		Iterator<String> worditer = wordlist.iterator();
		String tableName = tag == true? SPAM_DICT_TABLE_NAME : NORMAL_DICT_TABLE_NAME;
		ResultSet rs = null;
		String thisWord = null;
		HashMap<String,Integer> hashResult = new HashMap<String,Integer>();
		String queryStatement = null;
				
		while(worditer.hasNext())
		{
			thisWord = worditer.next();
			queryStatement = "SELECT * FROM " + tableName + " WHERE Dict_word = '" + thisWord + "'" ;
			if(debugMode)
				System.out.println(queryStatement);
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(queryStatement);
				boolean hasNext = rs.next();
				if(debugMode)
					System.out.println(hasNext);
				if(!hasNext)
				{
					System.out.println("no this word : " + thisWord);
					if(isInsert)
					{
						insert(tag, thisWord);
						hashResult.put(thisWord, 1);
					}
					else
						hashResult.put(thisWord, 0);
				}
				else
				{
					hashResult.put(thisWord, rs.getInt("Dict_frequency"));
				}
			} catch (SQLException e) {
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
	 * 		ON DUPLICATE KEY UPDATE Dict_frequency = Dict_frquency + 1;
	 * UPDATE $tableName SET Dict_frequency = Dict_frequency  + 1 WHERE Dict_word = $sumName;
	 * 
	 * It's lucky that MySQL supports duplication detectation.If the word exists,Dict_frequency ++;if not, create a new record.
	 * It works automatically!
	 * @param tag insert into `spam_dict` or `normal_dict` ,true stands for `spam_dict`
	 * */
	private int insert(boolean tag,String word) {
		String tableName = tag == true ? SPAM_DICT_TABLE_NAME : NORMAL_DICT_TABLE_NAME;
		String insertStatement = "INSERT INTO " + tableName + " (Dict_word, Dict_frequency) VALUES ('" +  word + "', 1)"
				+ "ON DUPLICATE KEY UPDATE Dict_frequency = Dict_frequency + 1";
		String addSumRecord = "UPDATE " + tableName + " SET Dict_frequency = Dict_frequency + 1 " + "WHERE Dict_word = '" + SUM_NAME + "'";//sum ++
		if(debugMode)
		{
			System.out.println(insertStatement);
			System.out.println(addSumRecord);
		}
		
		try {
			statement.executeUpdate(insertStatement);
			statement.executeUpdate(addSumRecord);
		} catch (SQLException e) {
			System.out.println(word + "Insert Failed");
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
		if(debugMode)
		{
			System.out.println(deleteStatement);
		}
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
	
	/**
	 * delete a list of words
	 * */
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
	 * DELETE FROM $table where Mail_id = $ID;
	 * 
	 * Mail database
	 * delete method should avoid user from missing to delete words in words_dict. 
	 * */
	@Override
	public int delete(long ID) {
		conn = getConnection();
		String deleteStatement = "DELETE FROM " + MAIL_TABLE_NAME + "WHERE Mail_id = " + ID;
		if(debugMode)
		{
			System.out.println(deleteStatement);
		}
		try {
			statement = conn.createStatement();
			statement.executeUpdate(deleteStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("delete mail failed");
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	
	/**
	 * INSERT INTO $table (Mail_content, Mail_tag) values($content, $tag);
	 * 
	 * SELECT LAST_INSERT_ID();
	 * 
	 * ATTENTION: In the database, ($author, $subject, $date) of Email is not stored.
	 * insert a mail into mail table
	 * */
	@Override
	public int insert(Mail mail) {
		conn = getConnection();
		String insertStatement = "INSERT INTO "+ MAIL_TABLE_NAME + " (Mail_content, Mail_tag) values(\n" + 
				"'" + mail.getContent() + "',\n"
						 + (mail.isSpam() == true ? "true" : "false") +")";
		String queryIdStatement = "SELECT LAST_INSERT_ID()";
		ResultSet rs;
		
		if(debugMode)
		{
			System.out.println(insertStatement);	
		}
		try {
			statement = conn.createStatement();
			statement.executeUpdate(insertStatement);
			rs = statement.executeQuery(queryIdStatement);
			if(rs.next())
				mail.setId(rs.getLong("LAST_INSERT_ID()"));
			
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("insert mail failed");
			e.printStackTrace();
		}	
		return 0;
	}

	
	/**
	 * SELECT * FROM $table WHERE Mail_id = $ID;
	 * 
	 * search a mail from database by ID
	 * */
	@Override
	public Mail query(long ID) {
		conn = getConnection();
		String queryStatement = "SELECT * FROM " + MAIL_TABLE_NAME + " WHERE Mail_id = " + ID;
		Mail mail = new Email();
		ResultSet rs;
		if(debugMode)
		{
			System.out.println(queryStatement);
		}
		
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(queryStatement);
			boolean hasNext = rs.next();
			if(debugMode)
				System.out.println(hasNext);
			if(hasNext)
			{
				mail.setContent(rs.getString(MailDataIndex.INDEX_CONTENT.ordinal()));
				mail.setTag(rs.getBoolean(MailDataIndex.INDEX_TAG.ordinal()));
				mail.setId(ID);
			}
			else
			{
				System.out.println("No Mail whose id is :" + ID);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}	
		return mail;
	}
	
	public ResultSet query(String execuStatement) throws SQLException{
		conn = getConnection();
		if(debugMode)
		{
			System.out.println(statement);
		}
		this.statement = conn.createStatement();
		return statement.executeQuery(execuStatement); 

	}
}
