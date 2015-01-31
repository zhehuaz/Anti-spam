package org.bit.train;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.bit.conn.DictAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Mail;

public class Trainer {
	
	/** 
	 * This class works as a trainer,which contains series of methods implements Bayes Method.
	 * P(S|w) means the probability that the mail is spam in condition that word `w` show up in it.
	 * @parame driver,url,user,password A trainer is suppose to know the information to obtain the access to database of dictionary. 
	 * @throws SQLException 
	 * */
	Trainer(String driver,String url,String user,String password) throws SQLException,UnknownDBException{
		switch(driver){
		case "com.mysql.jdbd.Driver": dictAccess =  new MysqlAccess(url,user,password);break;
		default: dictAccess = null;throw new UnknownDBException("Database type Unknown");
		}
	}
	
	/** contains words shown in mail,and the P(S|w) of each word is of Double*/
	private DictAccess dictAccess;
	
	/** 
	 * train with a mail but don't save it
	 * @param mail is intend for training
	 * @return number of words in mail
	 * */
	public int train(Mail mail)
	{
		dictAccess.insert(mail.isSpam(),mail.getWordlist());
		return 0;
	}
	
	/** If the `tag` is incorrect,undo the train...
	 * @param mail REMEBER mail's tag is NEW tag,after retrain.
	 * */
	public int untrain(Mail mail)
	{
		dictAccess.delete(!mail.isSpam(), mail.getWordlist());
		return 0;
	}
}
