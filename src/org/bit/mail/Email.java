package org.bit.mail;

import java.util.Date;

public class Email extends Mail{

	String subject;
	String author;
	Date date;
	
	/**
	 * Assume that input is already content. 
	 * */
	@Override
	public int parseText(String input) {
		
		return 0;
	}

}
