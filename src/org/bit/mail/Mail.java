package org.bit.mail;

import java.util.HashMap;

public abstract class Mail {
	/** FIXME The procedure is wired. new() -> parse()(sub) -> load()(super) */
	
	
	private long id;
	private String content;
	private boolean tag;//if this mail is spam
	
	/**	load text of mail body */
	public abstract int parseText(String input);

	private HashMap<String,Double> wordsProb;
	
	private int loadWords()
	{
		// TODO be careful when text == null
		// TODO load words into HashMap from parseText()
		
		return 0;
	}
	
	public HashMap<String, Double> getWordsProb() {
		return wordsProb;
	}

	public boolean isTag() {
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
}
