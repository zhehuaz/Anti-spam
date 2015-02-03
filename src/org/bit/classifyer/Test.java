package org.bit.classifyer;

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
			Classifyer classify = new Classifyer("com.mysql.jdbc.Driver", url, user, password);
			mailAccess = new MysqlAccess(url, user, password);
			System.out.println("This mail is " + (classify.classify(mailAccess.query(7)) ? "Spam" : "Healthy") + ".");
		} catch (UnknownDBException | SQLException e) {
			e.printStackTrace();
		}
	}
}
