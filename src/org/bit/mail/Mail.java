package org.bit.mail;

import java.util.HashMap;

public abstract class Mail {
	
	private long ID;
	private String text;
	
	/**	load text of mail body */
	public abstract int parseText(String input);

	private HashMap<String,Double> wordsProb;
	
	public HashMap<String, Double> getWordsProb() {
		return wordsProb;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
