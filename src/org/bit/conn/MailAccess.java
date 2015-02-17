package org.bit.conn;

import java.sql.SQLException;
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
	
	public int createTableMail() throws SQLException;
	public Mail query(long ID) throws SQLException;
	public int delete(long ID) throws SQLException;
	public int insert(Mail mail) throws SQLException;
	
}
