package com.marcos.processor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.processor.dto.input.GenerateComparisonConnectorInputDTO;
import com.marcos.processor.dto.input.GenerateConnectorListInputDTO;
import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.service.ConnectorService;

@RestController
@RequestMapping("/connector")
public class ConnectorController {
	
	@Autowired
	private ConnectorService service;
	
	@PostMapping
	public ResponseEntity<String> extractRequestsOfConnectors(@RequestBody GenerateConnectorListInputDTO inputData) {
		
		service.buildExportFiles(inputData);
		
		String response =  service.getPathSavedFiles();
		
		response = "Arquivos salvos em: " + response;
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/comparison")
	public void comparisonConnectors(@RequestBody GenerateComparisonConnectorInputDTO inputData) {
		this.service.connectorComparison(inputData);
	}
}
