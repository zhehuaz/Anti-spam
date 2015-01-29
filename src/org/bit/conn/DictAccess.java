package org.bit.conn;

import java.util.HashMap;
import java.util.List;

public interface DictAccess extends DatabaseAccess{
	
	public int CreateTableDict();
	public HashMap<String,Integer> Query(boolean tag,List<String> wordlist);
	public int Delete(boolean tag,String word);//minus 1 of frequency
	public int Insert(boolean tag,String word);
	public int Modify(boolean tag,String word);
}
