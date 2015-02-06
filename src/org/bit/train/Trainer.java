package org.bit.train;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.bit.conn.DictAccess;
import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Mail;

public class Trainer {
	
	/** 
	 * This class works as a trainer,which contains series of methods implements Bayes Method.
	 * P(S|w) means the probability that the mail is spam in condition that word `w` show up in it.
	 * @parame driver,url,user,password A trainer is suppose to know the information to obtain the access to database of dictionary. 
	 * @throws SQLException 
	 * */
	public Trainer(String driver,String url,String user,String password) throws SQLException,UnknownDBException{
		switch(driver){
		case "com.mysql.jdbc.Driver": dictAccess =  new MysqlAccess(url,user,password);
									mailAccess = new MysqlAccess(url,user,password);
											break;
		default: dictAccess = null;throw new UnknownDBException("Database type Unknown");
		}
	}
	
	public Trainer(String driver,String dictAccessPropPath,String mailAccessPropPath) throws UnknownDBException, FileNotFoundException, IOException, SQLException{
		switch(driver){
		case "com.mysql.jdbc.Driver": this.dictAccess = new MysqlAccess(dictAccessPropPath);
									this.mailAccess = new MysqlAccess(mailAccessPropPath);
											break;
		default: dictAccess = null;throw new UnknownDBException("Database type Unknown");
		}
		
	}
	
	/** contains words shown in mail,and the P(S|w) of each word is of Double*/
	private DictAccess dictAccess;
	private MailAccess mailAccess;
	
	/** 
	 * train with a mail but can optional save it
	 * @param mail is intend for training
	 * @return number of words in mail
	 * */
	public int train(Mail mail,boolean isSave)
	{
		mail.parseText();//remember to parse !!!
		dictAccess.insert(mail.isSpam(),mail.getWordlist());
		if(isSave)
			mailAccess.insert(mail);
		return 0;
	}
	
	/** If the classify result is incorrect,undo the train...
	 * @param mail REMEBER mail's tag is OLD tag,before retrain.
	 * */
	public int untrain(Mail mail)
	{
		dictAccess.delete(mail.isSpam(), mail.getWordlist());
		return 0;
	}
	
	public int untrain(long id){
		untrain(mailAccess.query(id));
		return 0;
	}
}
