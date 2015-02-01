package org.bit.mail;

import java.io.IOException;

import org.bit.util.SegmentWords;
import org.lionsoul.jcseg.core.JcsegException;


public class ShortMessage extends Mail{

	@Override
	public void parseText() {
		// TODO directly generate and store wordlist in thid method
		try {
			wordlist = SegmentWords.segment(content);
		} catch (JcsegException | IOException e) {
			e.printStackTrace();
		}
	}

}
