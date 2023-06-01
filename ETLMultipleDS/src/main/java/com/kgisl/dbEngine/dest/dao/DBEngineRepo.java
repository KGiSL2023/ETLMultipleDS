package com.kgisl.dbEngine.dest.dao;

import java.util.List;
import java.util.Map;

public interface DBEngineRepo {

	Map<String, String> fetchColumnType(String tableName, String dbName);

	void executeQuery(String insertionQuery);
	
	public Map<String, List<String>> getTables() throws Exception ;

}
