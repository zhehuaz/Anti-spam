package org.bit.util;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.lionsoul.jcseg.core.ADictionary;
import org.lionsoul.jcseg.core.DictionaryFactory;
import org.lionsoul.jcseg.core.ISegment;
import org.lionsoul.jcseg.core.IWord;
import org.lionsoul.jcseg.core.JcsegException;
import org.lionsoul.jcseg.core.JcsegTaskConfig;
import org.lionsoul.jcseg.core.SegmentFactory;

public class SegmentWords{
	
	private final static String propPath = "/home/langley/Install/apache-tomcat-8.0.17/webapps/Anti-Spam/WEB-INF/lib/jcseg.properties";
	
	public static void trim(String sentence){
		//TODO to cut tags and '\n'
	}
	
	/**
	 * @praram sentence this sentence should be pure,which has no tag like "<tag>" or "\n".
	 * */
	public static ArrayList<String> segment(String sentence) throws JcsegException, IOException{
		trim(sentence);
		ArrayList<String> words = new ArrayList<String>();
		JcsegTaskConfig config = new JcsegTaskConfig(propPath);
		ADictionary dic = DictionaryFactory.createDefaultDictionary(config);
		ISegment seg = SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE , new Object[]{config,dic});
		seg.reset(new StringReader(sentence));
		IWord word = null;
		while((word = seg.next()) != null)
			words.add(word.getValue());
		return words;
	}
}