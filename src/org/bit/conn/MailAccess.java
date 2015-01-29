package org.bit.conn;

import java.util.HashMap;

import org.bit.mail.Mail;

public interface MailAccess extends DatabaseAccess{

	public int CreateTableMail();
	//public HashMap<String,Integer> Query();
	public int Delete(Mail mail);
	public int Insert(Mail mail);
	public int Modify(Mail mail);
}
