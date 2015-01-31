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
	
	public static void trim(String sentence){
		
	}
	
	
	
	/**
	 * @praram sentence this sentence should be pure,which has no segments like "<tag>" or "\n".
	 * */
	public static ArrayList<String> segment(String sentence) throws JcsegException, IOException{		
		ArrayList<String> words = new ArrayList<String>();
		JcsegTaskConfig config = new JcsegTaskConfig(null);
		ADictionary dic = DictionaryFactory.createDefaultDictionary(config);
		ISegment seg = SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE , new Object[]{config,dic});
		seg.reset(new StringReader(sentence));
		IWord word = null;
		while((word = seg.next()) != null)
			words.add(word.getValue());
		return words;
	}
}