package org.bit.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.bit.util.SegmentWords;
import org.lionsoul.jcseg.core.JcsegException;

public class Email extends Mail{

	String subject;
	String author;
	Date date;
	
	/**
	 * Assume that input is already content. 
	 * */
	@Override
	public ArrayList<String> parseText(String input) {
		ArrayList<String> list = null;
		try {
			list = SegmentWords.segment(input);
		} catch (JcsegException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
