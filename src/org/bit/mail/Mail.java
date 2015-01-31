package org.bit.mail;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Mail {
	
	private long id;
	private String content;//not trimmed
	private boolean tag;//if this mail is spam
	private String from;//sender of the mail
	protected ArrayList<String> wordlist;
	
	public ArrayList<String> getWordlist() {
		return wordlist;
	}

	/*static word into word-list from text of mail */
	public abstract void parseText(String input);

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
