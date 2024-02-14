package com.marcos.processor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.processor.dto.input.GenerateConnectorListInputDTO;
import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.service.ConnectorService;

@RestController
@RequestMapping("/connector")
public class ConnectorController {
	
	@Autowired
	private ConnectorService service;
	
	@PostMapping
	public ResponseEntity<ConnectorList> extractRequestsOfConnectors(@RequestBody GenerateConnectorListInputDTO inputData) {
		
		var response =  service.getConnectorList(inputData);
		return ResponseEntity.ok(response);
		
	}
}
