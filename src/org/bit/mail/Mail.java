package org.bit.mail;

public abstract class Mail {
	
	private long ID;
	private String text;
	
	public abstract int parseText(String input);

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
