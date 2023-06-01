package com.kgisl.dbEngine.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kgisl.dbEngine.model.source.EisTrnCoverNoteExt;
import com.kgisl.dbEngine.model.source.QueryParamModel;

public interface DBEngineService {

	Map<String, List<String>> getSourceForUI() throws Exception;

	Map<String, List<String>> getDestinationForUI() throws Exception;
	
	void copyColumnData(QueryParamModel queryParamModel, JSONArray jsonArray) throws JsonProcessingException, ParseException;

	List<EisTrnCoverNoteExt> getAllCoverages();

	JSONArray covertToJson(List<?> entityObj) throws JsonProcessingException;

}
