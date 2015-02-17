package org.bit.classifyer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bit.conn.DictAccess;
import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Mail;
import org.bit.train.Trainer;
import org.bit.train.UnknownDBException;

public class Classifyer {
	
	DictAccess dictAccess;
	Trainer trainer;
	private final static double INF = 1e-5; 
	private final static double PROB_OF_SPAM = 0.5;
	private final static double PROB_OF_NORMAL = 0.5;
	private final static double PROB_IF_NOT_EXISTS_IN_NORMAL = 0.01;
	private final static int WORD_CONTAIN = 20;//how many top words you want
	private final static double THRESHOLD_OF_SPAM = 0.9;
	
	public Classifyer(String driver,String url,String user,String password) throws UnknownDBException, SQLException{
		switch(driver){
		case "com.mysql.jdbc.Driver": dictAccess = new MysqlAccess(url,user,password);break;
		default: dictAccess = null;throw new UnknownDBException("Database type Unknown");
		}
	}
	
	public Classifyer(String driver,String dictAccessPropPath) throws UnknownDBException, FileNotFoundException, IOException, SQLException
	{
		switch(driver){
		case "com.mysql.jdbc.Driver": dictAccess = new MysqlAccess(dictAccessPropPath);break;
		default: dictAccess = null;throw new UnknownDBException("Database type Unknown");
		}
	}
	
	/**
	 * Calculate P(S|w) = (P(w|S)P(S)) / (P(w|S)P(S) + P(w|N)P(N))
	 * We assume that P(S) = P(N) = 50%
	 * So I need @param probwS as P(w|S) and @param probwN as P(w|N)
	 * if a word only appears in Spam_Dict, we assume P(w|N) = 1%
	 * @return P(S|w) 
	 * */
	private Double probOfWordByBayes(double probwS,double probwN,double ps,double pn)
	{
		double ans;
		if(probwN < INF && probwS > INF)
			probwN = PROB_IF_NOT_EXISTS_IN_NORMAL;
		ans = (probwS * ps) / ((probwS * ps) + (probwN * pn));
		return ans;
		
	}
	
	/**
	 * Cal the final probability of spam
	 * P = (P1 * P2 * P3 * ...) / ( (P1 * P2 * P3 * ...) + ((1 - P1) * (1 - P2) * (1- P3) * ...) )
	 * ATTENTION : there should not be 0 in Pi!
	 * */
	private double probOfSpam(ArrayList<Entry<String,Double>> wordArray){
		double prob = 1;
		double multi = 1;
		double nagMulti = 1;
		for(Entry<String,Double> e : wordArray){
			multi *= e.getValue();
			nagMulti *= (1 - e.getValue());
		}
		prob = multi / (multi + nagMulti);
		
		return prob;
	}
	
	/** sort P(S|w) of words in <code>HashMap<String,Double> words</code> 
	 * @param num how many top entries returned . '0' means all.
	 * */
	@SuppressWarnings("unchecked")
	private ArrayList<Entry<String,Double>> sortProbabilityOfWords(Map<String,Double> probMap,int num)
	{
		// TODO sort words by P(S|w)
		ArrayList<Entry<String,Double>> array = new ArrayList<Entry<String,Double>>(probMap.entrySet());
		Collections.sort(array,new Comparator<Map.Entry<String,Double>>(){
			@Override
			public int compare(Entry<String, Double> arg0,
					Entry<String, Double> arg1) {
				if(arg0.getValue() > arg1.getValue())
					return 1;
				else
					return 0;
			}
		});
		
		if(num <= 0){
			Iterator iter = array.iterator();
			for(int i = 0;i < num && iter.hasNext();i ++)
				if(((Entry<String,Double>)iter.next()).getValue() < INF)
					break;
			while(iter.hasNext())
				iter.remove();
		}
		
		return array;
	}
	
	/** Classify the Mail
	 * @return <code>TRUE</code> means the mail is spam
	 * @throws SQLException 
	 * */
	public boolean classify(Mail mail) throws SQLException
	{
		// TODO train the dic after classify a mail 
		mail.parseText();
		
		HashMap<String,Integer> rsOfSpam = dictAccess.query(true, true, mail.getWordlist());
		HashMap<String,Integer> rsOfNormal = dictAccess.query(false, false, mail.getWordlist());
		
		/* P(S|w) of each word*/
		HashMap<String,Double> probMap = new HashMap<String, Double>();
		
		double sumInSpam = (double)rsOfSpam.get(MysqlAccess.SUM_NAME);
		double sumInNormal = (double)rsOfNormal.get(MysqlAccess.SUM_NAME);
		for(Entry<String, Integer> m: rsOfSpam.entrySet())
		{
			if(m.getKey() != MysqlAccess.SUM_NAME)
				probMap.put(m.getKey(), probOfWordByBayes(m.getValue() / sumInSpam, rsOfNormal.get(m.getKey()) / sumInNormal, PROB_OF_SPAM, PROB_OF_NORMAL));
		}
		ArrayList<Entry<String,Double>> topWords = sortProbabilityOfWords(probMap, WORD_CONTAIN);
		mail.setTag(probOfSpam(topWords) > THRESHOLD_OF_SPAM);
		
		return mail.isSpam();
	}
}
