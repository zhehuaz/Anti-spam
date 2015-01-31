package org.bit.conn;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bit.mail.Email;
import org.bit.mail.Mail;

public class DatabaseTest {

	public static void main(String [] args)
	{
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("sqlInfo.ini"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = prop.getProperty("url");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		MysqlAccess ma = new MysqlAccess(url,user,password);
		/*HashMap<String,Integer> frequency;
		ma.createTableDict();
		ma.createTableMail();
		
		System.out.println("In Spam Dictionary :");
		List<String> spamWords = new LinkedList<String>();
		spamWords.add("惊喜");
		spamWords.add("欢迎");
		spamWords.add("恭喜");
		spamWords.add("诱惑");
		spamWords.add("实惠");
		spamWords.add("中奖");
		spamWords.add("购买");
		frequency = ma.query(true,false, spamWords);
		for(Map.Entry<String, Integer> m: frequency.entrySet())
		{
			System.out.println(m.getKey() + "-" + m.getValue());
		}
		
		System.out.println("In Normal Dictionary :");
		List<String> normalWords = new LinkedList<String>();
		normalWords.add("您好");
		normalWords.add("好吧");
		normalWords.add("欢迎");
		normalWords.add("老师");
		normalWords.add("恭喜");
		normalWords.add("谢谢");
		normalWords.add("聚会");
		
		frequency = ma.query(false,false,normalWords);
		for(Map.Entry<String, Integer> m: frequency.entrySet())
		{
			System.out.println(m.getKey() + "-" + m.getValue());
		}
		*/
		
		ma.createTableMail();
		
		Mail mail = new Email();
		mail.setContent("<br><br>发货清单：<br>1，南孚5号电池：4节。<br>2，南孚7号电池：2节<br><br>一共0.94元，&nbsp;需要拍2个套餐产品。<br><br>套餐1：南孚5号电池：4节，0.88元。&nbsp;&nbsp;<a href=\"http://url7.me/kuEv\" target=\"_blank\">http://url7.<wbr>me/kuEv</a><br>套餐2：南孚7号电池：2节，0.06元。&nbsp;&nbsp;<a href=\"http://url7.me/gEFv\" target=\"_blank\">http://url7.<wbr>me/gEFv</a><br><br>&nbsp;<br>请&nbsp;把这2个套餐&nbsp;全拍了&nbsp;。我们一起给你发货.&nbsp;<br><br><br>冲皇冠，大促销，确保正品。&nbsp;请收到货好评。<br><br><br>");
		mail.setTag(true);
		ma.insert(mail);
		Mail getMail = ma.query(mail.getId());
		System.out.println("The content of " + getMail.getId() + " : " + getMail.getContent());
	}
}
