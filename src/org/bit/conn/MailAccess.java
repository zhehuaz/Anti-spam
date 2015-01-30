package org.bit.conn;

import java.util.HashMap;

import org.bit.mail.Mail;

public interface MailAccess extends DatabaseAccess{

	public int createTableMail();
	//public HashMap<String,Integer> Query();
	public int delete(Mail mail);
	public int insert(Mail mail);
	public int modify(Mail mail);
}
