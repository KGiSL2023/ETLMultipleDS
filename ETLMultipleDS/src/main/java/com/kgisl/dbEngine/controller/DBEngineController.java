package com.kgisl.dbEngine.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kgisl.dbEngine.model.source.EisTrnCoverNoteExt;
import com.kgisl.dbEngine.model.source.QueryParamModel;
import com.kgisl.dbEngine.service.DBEngineService;

@RestController
@RequestMapping("/")
public class DBEngineController {

	@Autowired
	DBEngineService dbEngineService;
	
	Logger logger = LoggerFactory.getLogger(DBEngineController.class);
	
	@GetMapping("/getSource")
	public Map<String,List<String>> getSourceForUI(){
		try {
		Map<String, List<String>> tableColumnMap = dbEngineService.getSourceForUI();
		return tableColumnMap;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/getDest")
	public Map<String,List<String>> getDestinationForUI(){
		try {
		Map<String, List<String>> tableColumnMap = dbEngineService.getDestinationForUI();
		return tableColumnMap;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@GetMapping("/getAllCoverageDetails")
	public List<EisTrnCoverNoteExt> getCoverageDetails() {

		List<EisTrnCoverNoteExt> coverageDetails = (List<EisTrnCoverNoteExt>) dbEngineService.getAllCoverages();

		return coverageDetails;

	}
	
	@PostMapping("/copyCoverageDetails")
	public void copyCoverageDetails(@RequestBody QueryParamModel queryParamModel) throws JsonProcessingException, ParseException {

		List<EisTrnCoverNoteExt> coverageDetails = getCoverageDetails();
		dbEngineService.copyColumnData(queryParamModel, dbEngineService.covertToJson(coverageDetails));
	}
	
}
