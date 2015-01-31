package org.bit.classify;

import java.util.HashMap;
import java.util.Map;

import org.bit.conn.DictAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Mail;
import org.bit.train.Trainer;

public class Classify {
	
	DictAccess dictAccess;
	Trainer trainer;
	
	
	/** sort P(S|w) of words in <code>HashMap<String,Double> words</code> */
	private void sortProbabilityOfWords()
	{
		// TODO sort words by P(S|w)
		
	}
	
	/** Classify the Mail
	 * @return <code>TRUE</code> means the mail is spam
	 * */
	public boolean Classcify(Mail mail)
	{
		// TODO train the dic after classify a mail 
		HashMap<String,Integer> rs = dictAccess.query(mail.isSpam(), true, mail.getWordlist());
		/* P(S|w) of P(N|w) of each word*/
		HashMap<String,Double> probMap = new HashMap<String, Double>();
		
		double sum = (double)rs.get(MysqlAccess.SUM_NAME);
		for(Map.Entry<String, Integer> m: rs.entrySet())
		{
			//I don't delete SUM to save time here,so careful of the result after sorted
			probMap.put(m.getKey(), m.getValue() / sum);
		}
		return false;
	}
}
