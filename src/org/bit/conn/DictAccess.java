package org.bit.conn;

import java.util.HashMap;
import java.util.List;

/**
* CREATE TABLE Spam_Dictionary(
* 		Dict_word TEXT UNIQUE NOT NULL,
* 		Dict_frequency INTEGER UNSIGNED NOT NULL;
* );
* 
* CREATE TABLE Normal_Dictionary(
* 		Dict_word TEXT UNIQUE NOT NULL,
* 		Dict_frequency INTEGER UNSIGNED NOT NULL;
* );
*
* */
public interface DictAccess extends DatabaseAccess{
	
	enum DictDataIndex{INDEX_WORD, INDEX_FREQUENCY };
	
	public int createTableDict();
	public HashMap<String,Integer> query(boolean tag, boolean isInsert, List<String> wordlist);
	public int delete(boolean tag,List<String> word);//minus 1 of frequency
	public int insert(boolean tag,List<String> word);

}
