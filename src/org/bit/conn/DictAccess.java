package org.bit.conn;

import java.util.HashMap;
import java.util.List;

public interface DictAccess extends DatabaseAccess{
	
	public int createTableDict();
	public HashMap<String,Integer> query(boolean tag, boolean isInsert, List<String> wordlist);
	public int delete(boolean tag,List<String> word);//minus 1 of frequency
	public int insert(boolean tag,List<String> word);

}
