package org.bit.classify;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.train.UnknownDBException;

public class Test {

	public static void main(String []args){
		MailAccess mailAccess;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("sqlInfo.ini"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = prop.getProperty("url");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		try {
			Classify classify = new Classify("com.mysql.jdbc.Driver", url, user, password);
			mailAccess = new MysqlAccess(url, user, password);
			System.out.println("This mail is " + (classify.classify(mailAccess.query(7)) ? "Spam" : "Healthy") + ".");
		} catch (UnknownDBException | SQLException e) {
			e.printStackTrace();
		}
	}
}
