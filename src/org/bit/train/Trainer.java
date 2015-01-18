package org.bit.train;

import java.util.HashMap;

import org.bit.conn.DictAccess;
import org.bit.mail.Mail;

public class Trainer {
	
	/** 
	 * This class works as a trainer,which contains series of methods implements Bayes Method.
	 * P(S|w) means the probability that the mail is spam in condition that word `w` show up in it.
	 * */

	
	/** contains words shown in mail,and the P(S|w) of each word is of Double*/
	private HashMap<String, Double> words;
	private DictAccess dictAccess;
	
	/** sort P(S|w) of words in <code>HashMap<String,Double> words</code> */
	private void sortProbabilityOfWords()
	{
		// TODO sort words by P(S|w)
		
	}
	
	/** 
	 * @param mail is intend for training
	 * @param tag marks whether this mail is spam or not.<code>TRUE</code> means it is.
	 * @return word number of mail
	 * 
	 * */
	public int train(Mail mail,boolean tag)
	{
		// TODO train using `words`
		// TODO for UNTRAIN and RETRAIN ,remember to save mails you've trained!!!
		return 0;
	}
	
	/** If the `tag` is incorrect,undo the train...And retrain :D */
	public int untrain(Mail mail)
	{
		
		return 0;
	}
}
