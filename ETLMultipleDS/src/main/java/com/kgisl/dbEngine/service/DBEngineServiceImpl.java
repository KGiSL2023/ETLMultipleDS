package com.kgisl.dbEngine.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgisl.dbEngine.dest.dao.DBEngineDestRepoImpl;
import com.kgisl.dbEngine.model.source.EisTrnCoverNoteExt;
import com.kgisl.dbEngine.model.source.QueryParamModel;
import com.kgisl.dbEngine.src.dao.DBEngineSourceRepoImpl;
import com.kgisl.dbEngine.src.dao.EisTrnCoverNoteExtRepo;

@Service
public class DBEngineServiceImpl implements DBEngineService {
	
	Logger logger = LoggerFactory.getLogger(DBEngineService.class);

	@Autowired
	DBEngineSourceRepoImpl sourceDao;
	
	@Autowired
	DBEngineDestRepoImpl destDao;
	
	@Autowired
	private EisTrnCoverNoteExtRepo eisTrnCoverNoteExtRepo;

	@Override
	public Map<String, List<String>> getSourceForUI() throws Exception {
		return sourceDao.getTables();
	}

	@Override
	public Map<String, List<String>> getDestinationForUI() throws Exception {
		return destDao.getTables();
	}

	public List<EisTrnCoverNoteExt> getAllCoverages() {

		return (List<EisTrnCoverNoteExt>) eisTrnCoverNoteExtRepo.findAll();
	}

	private void generateInsertionQueryAndExecute(HashMap<String, Object> nameValuePairs, String destTable,
			String dbName) {

		Map<String, String> datatypeMapper = destDao.fetchColumnType(destTable, dbName);

		String insertionQuery = constructInsertionQuery(datatypeMapper, nameValuePairs, destTable, dbName);

		destDao.executeQuery(insertionQuery);

	}

	private String constructInsertionQuery(Map<String, String> datatypeMapper, HashMap<String, Object> nameValuePairs,
			String destTable, String dbName) {

		String insertionQuery = "insert into " + dbName + "." + destTable + " (";
		String columnNames = "";
		String values = "";
		for (Map.Entry<String, ?> entry : nameValuePairs.entrySet()) {
			if (columnNames.isEmpty())
				columnNames = columnNames + entry.getKey();
			else
				columnNames = columnNames + "," + entry.getKey();

			if (StringUtils.containsIgnoreCase(datatypeMapper.get(entry.getKey().toUpperCase()), "varchar")
					|| StringUtils.containsIgnoreCase(datatypeMapper.get(entry.getKey().toUpperCase()), "char")) {
				if (!(entry.getValue().toString().startsWith("'") && entry.getValue().toString().endsWith("'")))
					nameValuePairs.put(entry.getKey(), "'" + entry.getValue() + "'");
			}
			
			if(StringUtils.containsIgnoreCase(datatypeMapper.get(entry.getKey().toUpperCase()), "number")||
					StringUtils.containsIgnoreCase(datatypeMapper.get(entry.getKey()), "int"))
				nameValuePairs.put(entry.getKey(), Integer.valueOf((entry.getValue().toString())));
			
			if(StringUtils.containsIgnoreCase(datatypeMapper.get(entry.getKey().toUpperCase()), "double"))
				nameValuePairs.put(entry.getKey(), Double.valueOf((entry.getValue().toString())));

			if (values.isEmpty())
				values = values + entry.getValue();
			else
				values = values + "," + entry.getValue();
		}

		insertionQuery = insertionQuery + columnNames + ") values(" + values + ")";

		return insertionQuery;
	}

	public JSONArray covertToJson(List<?> entityObj) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(entityObj);
		System.out.println(jsonString);
		JSONArray array = new JSONArray(jsonString);
		return array;
	}

	@Transactional("destTransactionManager")
	public void copyColumnData(QueryParamModel queryParamModel, JSONArray jsonArray) throws JsonProcessingException, ParseException {

		String sourceDB = queryParamModel.getSourceDB();
		String destDB = queryParamModel.getDestDB();
		Map<String, Boolean> parentExecuted = new HashMap<String, Boolean>();

//		logger.info("SourceDB : " + sourceDB);
//		logger.info("DestDB : " + destDB);
		
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject srcJson = jsonArray.getJSONObject(i);

			for (Map.Entry<String, Map<String, ?>> tables : queryParamModel.getMapSourcetoDest().entrySet()) {

				HashMap<String, Object> nameValuePairs = new HashMap<String, Object>();

				String destParent = tables.getKey();

				parentExecuted.put(destParent, false); 

				for (Map.Entry<String, ?> entry : tables.getValue().entrySet()) {
					
					if(entry.getKey().contains("|")) {
						
						String[] sources = entry.getKey().split("\\|");
						HashMap<String,Object> concatenationValues = new HashMap<String, Object>();
						String concatenationString = "";
						for(String sourceCol : sources) {
							
							if (sourceCol.contains("SEQ_GEN")) {
//								++seq;
								getSequenceFields(sourceCol, entry.getValue().toString(), i+1, concatenationValues);
							}

							else if (sourceCol.contains("DATE")) {
								getDateFields(sourceCol, entry.getValue().toString(),srcJson, concatenationValues);
							}

							else if (entry.getKey().toString().contains(".")) {
								getFieldsFromSourceJSon(sourceCol, entry.getValue().toString(), srcJson, concatenationValues);
							}
							concatenationString = concatenationString + concatenationValues.get(entry.getValue().toString().split("\\.")[1]);
							concatenationValues.clear();
						}
						nameValuePairs.put(entry.getValue().toString().split("\\.")[1], concatenationString);
					}
						
					else if (entry.getKey().contains("SEQ_GEN")) {
						getSequenceFields(entry.getKey(), entry.getValue().toString(), i+1, nameValuePairs);
					}

					else if (entry.getKey().contains("DATE")) {
						getDateFields(entry.getKey(), entry.getValue().toString(), srcJson, nameValuePairs);
					}

					else if (entry.getKey().toString().contains(".")) 
						getFieldsFromSourceJSon(entry.getKey().toString(), entry.getValue().toString(), srcJson, nameValuePairs);
					

					else {

						if (!parentExecuted.get(destParent)) {
							parentExecuted.put(destParent, true);
							generateInsertionQueryAndExecute(nameValuePairs, destParent, destDB);
						}
						String childTable = entry.getKey().toString();

						HashMap<String, String> childTableMapping = (HashMap<String, String>) entry.getValue();

						Set<Map.Entry<String, String>> childTableEntry = childTableMapping.entrySet();

						JSONObject parentJson = srcJson.getJSONObject("parent");

						if ((JSONArray) parentJson.get("extra_coverage") != null) {

							getNestedJSon("extra_coverage", parentJson, nameValuePairs, childTableMapping,
									childTableEntry, childTable, destParent, destDB);
						}
						if ((JSONArray) parentJson.get("named_driver") != null) {

							getNestedJSon("named_driver", parentJson, nameValuePairs, childTableMapping,
									childTableEntry, childTable, destParent, destDB);

						}
					}

				}
				if (!parentExecuted.get(destParent))
					generateInsertionQueryAndExecute(nameValuePairs, destParent, destDB);
			}

		}

	}

	public HashMap<String, Object> getSequenceFields(String key, String value, int seq,
			HashMap<String, Object> nameValuePairs) {

//		++seq;
//		String formattedSeq = String.format("%02d", seq);
		if (key.contains("(")) {
			String[] values = key.substring(key.indexOf("(") + 1, key.indexOf(")")).split(",");
			String formattedSeq = String.format("%" + values[0] + "" + values[1] + "d", seq);
			nameValuePairs.put(value.split("\\.")[1], formattedSeq);
		} else
			nameValuePairs.put(value.split("\\.")[1], Integer.toString(seq));

		return nameValuePairs;

	}

	public HashMap<String, Object> getDateFields(String key, String value, JSONObject srcJson, HashMap<String, Object> nameValuePairs) throws ParseException {

		if (key.contains(".") && key.contains("(")) {
				
			String src = key.split("\\(")[0];
			getFieldsFromSourceJSon(src, value, srcJson, nameValuePairs);
			String srcData = nameValuePairs.get(value.split("\\.")[1]).toString();
			String formats = key.substring(key.indexOf("(") + 1, key.indexOf(")"));
			Date srcDate = new SimpleDateFormat(formats.split("\\,")[0])
                     .parse(srcData);
			 
			SimpleDateFormat destFormatter = new SimpleDateFormat(formats.split("\\,")[1]);
	        String formattedDate = destFormatter.format(srcDate);
			nameValuePairs.put(value.split("\\.")[1], formattedDate);
			
		}

		else {
			if (key.contains("(")) {
				String format = key.substring(key.indexOf("(") + 1, key.indexOf(")"));
				LocalDateTime datetime = LocalDateTime.now();
				DateTimeFormatter newPattern = DateTimeFormatter.ofPattern(format);
				String formattedDate = datetime.format(newPattern);
				nameValuePairs.put(value.split("\\.")[1], formattedDate);
			}

			else
				nameValuePairs.put(value.split("\\.")[1], "sysdate");
		}

		return nameValuePairs;

	}

	public HashMap<String, Object> getFieldsFromSourceJSon(String key, String value, JSONObject srcJson,
			HashMap<String, Object> nameValuePairs) {

		if (key.split("\\.")[1].contains("IDENTIFY")) {

			if (srcJson.has(key.split("\\.")[1].split(" IDENTIFY ")[1].toLowerCase())) {

				JSONObject obj1 = srcJson.getJSONObject(key.split("\\.")[1].split(" IDENTIFY ")[1].toLowerCase());

				nameValuePairs.put(value.toString().split("\\.")[1],
						obj1.getString(key.split("\\.")[1].split(" IDENTIFY ")[0].toString().toLowerCase()));
			} else {

				JSONObject parent = srcJson.getJSONObject("parent");

				JSONObject obj2 = parent.getJSONObject(key.split("\\.")[1].split(" IDENTIFY ")[1].toLowerCase());

				nameValuePairs.put(value.toString().split("\\.")[1],
						obj2.get(key.split("\\.")[1].split(" IDENTIFY ")[0].toString().toLowerCase()).toString());
			}

		} else {
			if (srcJson.has(key.split("\\.")[1].toString().toLowerCase()))
				nameValuePairs.put(value.toString().split("\\.")[1],
						srcJson.get(key.split("\\.")[1].toString().toLowerCase()).toString());
			else {
				JSONObject parent = srcJson.getJSONObject("parent");
				nameValuePairs.put(value.toString().split("\\.")[1],
						parent.get(key.split("\\.")[1].toString().toLowerCase()).toString());

			}
		}

		return nameValuePairs;

	}

	private HashMap<String, Object> getNestedJSon(String domain, JSONObject parentJson,
			HashMap<String, Object> nameValuePairs, HashMap<String, String> childTableMapping,
			Set<Map.Entry<String, String>> childTableEntry, String childTable, String destParent, String destDB) throws ParseException {

		HashMap<String, Object> childNameValuePairs = new HashMap<String, Object>();
		JSONArray ja = (JSONArray) parentJson.get(domain);
		for (int j = 0; j < ja.length(); j++) {
			JSONObject obj = ja.getJSONObject(j);

			for (String key : childTableMapping.keySet()) {

				if (key.equals("SEQ_GEN"))
					getSequenceFields(key, childTableMapping.get(key), j + 1, childNameValuePairs);

				else if (key.contains("DATE")) {
					getDateFields(key, childTableMapping.get(key), obj, childNameValuePairs);
				}

				else if (key.split("\\.")[0].equals(destParent)) {
					String value = nameValuePairs.get(key.split("\\.")[1]).toString();
					childNameValuePairs.put(childTableMapping.get(key).split("\\.")[1], value);
				}

				else if (key.split("\\.")[1].contains(" IDENTIFY ")
						&& obj.has(key.split("\\.")[1].split(" IDENTIFY ")[1].toLowerCase())) {

					JSONObject obj1 = obj.getJSONObject(key.split("\\.")[1].split(" IDENTIFY ")[1].toLowerCase());

					childNameValuePairs.put(childTableMapping.get(key).split("\\.")[1],
							obj1.get(key.split("\\.")[1].split(" IDENTIFY ")[0].toLowerCase()).toString());

				} else if (obj.has(key.split("\\.")[1].toString().toLowerCase())) {

					childNameValuePairs.put(childTableMapping.get(key).split("\\.")[1],
							obj.get(key.split("\\.")[1].toString().toLowerCase()).toString());
				} else
					return null;

			}
			generateInsertionQueryAndExecute(childNameValuePairs, childTable, destDB);
			childNameValuePairs.clear();
		}

		return childNameValuePairs;

	}

}
