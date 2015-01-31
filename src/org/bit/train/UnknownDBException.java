package org.bit.train;

public class UnknownDBException extends Exception{

	/**
	 * if the database type is unknown
	 * */
	public UnknownDBException(String msg) {
		super(msg);
	}
}
