package org.bit.conn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bit.mail.Email;
import org.bit.mail.Mail;
import org.bit.util.GlobalConstants;

public class DatabaseTest {

	/**
	 * This is only a test file for `MysqlAccess.java`.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * */
	public static void main(String [] args) throws SQLException, FileNotFoundException, IOException
	{
		MysqlAccess ma = new MysqlAccess(GlobalConstants.SQL_CONFIG_PATH);
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
		mail.setContent("发货清单：1，南孚5号电池：4节。2，南孚7号电池：2节  一共0.94元，  ;需要拍2个套餐产品。套餐1：南孚5号电池：4节，0.88元。 套餐2：南孚7号电池：2节，0.06元。http://url7.me/gEFv  请把这2个套餐全拍了。我们一起给你发货. 冲皇冠，大促销，确保正品。请收到货好评。");
		mail.setTag(true);
		ma.insert(mail);
		Mail getMail = ma.query(mail.getId());
		System.out.println("The content of " + getMail.getId() + " : " + getMail.getContent());
	}
}
