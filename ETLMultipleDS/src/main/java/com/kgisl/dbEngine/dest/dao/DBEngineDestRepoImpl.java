package com.kgisl.dbEngine.dest.dao;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class DBEngineDestRepoImpl implements DBEngineRepo {

	@Qualifier("destDatasource")
	@Autowired
	DataSource destDatasource;

	@Qualifier("destEntityManagerFactory")
	@Autowired
	private EntityManager entityManager;

	public Map<String, List<String>> getTables() throws Exception {
		Map<String, List<String>> tableColumnMap = new HashMap<>();
		DatabaseMetaData metaData = destDatasource.getConnection().getMetaData();
		ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
		while (tables.next()) {
			List<String> columnNames = new ArrayList<String>();
			String tableName = tables.getString("TABLE_NAME");
			System.out.println(tableName);
			ResultSet columns = metaData.getColumns(null, null, tableName, "%");
			while (columns.next()) {
				columnNames.add(columns.getString("COLUMN_NAME"));
				System.out.println("\t" + columns.getString("COLUMN_NAME"));
			}
			tableColumnMap.put(tableName, columnNames);
		}
		return tableColumnMap;
	}

	public Map<String, String> fetchColumnType(String tableName, String dbName) {

		String query = "SELECT COLUMN_NAME, DATA_TYPE FROM ALL_TAB_COLUMNS " + "WHERE OWNER =  '" + dbName
				+ "' and TABLE_NAME = '" + tableName + "'";
		
		List<Object[]> results = entityManager.createNativeQuery(query).getResultList();
		Map<String, String> columnDataTypeMapper = new HashMap<String, String>();
		for (Object[] result: results) {
		    columnDataTypeMapper.put((String)result[0], (String)result[1]);
		}

		return columnDataTypeMapper;
	}

	public void executeQuery(String insertionQuery) {
		entityManager.createNativeQuery(insertionQuery).executeUpdate();
	}

}
