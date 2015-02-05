package org.bit.classifyer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.train.UnknownDBException;

public class Test {

	public static void main(String []args) throws FileNotFoundException, IOException{
		MailAccess mailAccess;
		try {			
			Classifyer classifyer = new Classifyer("com.mysql.jdbc.Driver", "sqlInfo.ini");
			mailAccess = new MysqlAccess("sqlInfo.ini");
			System.out.println("This mail is " + (classifyer.classify(mailAccess.query(7)) ? "Spam" : "Healthy") + ".");
		} catch (UnknownDBException | SQLException e) {
			e.printStackTrace();
		}
	}
}
