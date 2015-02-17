package org.bit.conn;

import java.sql.SQLException;
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
* */
public interface DictAccess extends DatabaseAccess{
	
	enum DictDataIndex{BLANK, INDEX_WORD, INDEX_FREQUENCY };
	
	public HashMap<String,Integer> query(boolean tag, boolean isInsert, List<String> wordlist) throws SQLException;
	public int delete(boolean tag,List<String> word) throws SQLException;//minus 1 of frequency
	public int insert(boolean tag,List<String> word) throws SQLException;
	public int createTableDict() throws SQLException;

}
