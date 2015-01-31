package org.bit.mail;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Mail {
	
	private long id;
	private String content;//not trimmed
	private boolean tag;//if this mail is spam
	private String from;//sender of the mail
	
	/*static word into word-list from text of mail */
	public abstract ArrayList<String> parseText(String input);

	/*store probability of each word*/
	private HashMap<String,Double> wordsProb;
	
	public HashMap<String, Double> getWordsProb() {
		return wordsProb;
	}

	public boolean isSpam() {
		return tag;
	}

	public void setTag(boolean tag) {
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
