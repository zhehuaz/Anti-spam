package org.bit.conn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DatabaseTest {

	public static void main(String [] args)
	{
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "langley";
		String password = "";
		MysqlAccess ma = new MysqlAccess(url,user,password);
		
		ma.createTableDict();
		ma.createTableMail();
		
		List<String> spamWords = new LinkedList<String>();
		spamWords.add("惊喜");
		spamWords.add("欢迎");
		spamWords.add("恭喜");
		spamWords.add("诱惑");
		spamWords.add("实惠");
		spamWords.add("中奖");
		ma.insert(true, spamWords);
		
		List<String> normalWords = new LinkedList<String>();
		normalWords.add("您好");
		normalWords.add("新年");
		normalWords.add("老师");
		normalWords.add("恭喜");
		normalWords.add("谢谢");
		normalWords.add("聚会");
		
		HashMap<String,Integer> frequency = ma.query(false,true,normalWords);
		for(Map.Entry<String, Integer> m: frequency.entrySet())
		{
			System.out.println(m.getKey() + "-" + m.getValue());
		}
		
	}
}
