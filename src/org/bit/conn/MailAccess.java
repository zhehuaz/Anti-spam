package org.bit.conn;

import java.util.HashMap;

import org.bit.mail.Mail;


/**
 * CREATE TABLE Mail(
 * 		Mail_ID INTERGER AUTO_INCREMENT PRIMARY KEY,
 * 		Mail_content TEXT,
 * 		Mail_tag BOOL
 * );
 * */
public interface MailAccess extends DatabaseAccess{

	enum MailDataIndex {BLANK, INDEX_ID, INDEX_CONTENT, INDEX_TAG};
	
	public int createTableMail();
	public Mail query(long ID);
	public int delete(Mail mail);
	public int insert(Mail mail);
	
}
