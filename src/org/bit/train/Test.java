package org.bit.train;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;

public class Test {
	
	public static void main(String []args)
	{
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
			Trainer trainer = new Trainer("com.mysql.jdbc.Driver",url,user,password);
			MailAccess mailHelper = new MysqlAccess(url, user, password);
			trainer.train(mailHelper.query(5),false);
		} catch (SQLException | UnknownDBException e) {
			e.printStackTrace();
		}
		
	}
}
