package com.marcos.processor.event;

import org.springframework.stereotype.Component;

import com.marcos.processor.model.ConnectorList;

import lombok.Data;

@Data
@Component
public class GenerateConnectorEvent {

	private ConnectorList connectorList;
	private boolean generateCsvWithConnectorsName = true;
	private boolean generateJsonFile;
	private boolean generateCsvFile;
	private String fileName;
	private String jsonTargetPath;
	private String csvTargetPath;
	
	
	
	public GenerateConnectorEvent(ConnectorList connectorList, boolean generateJsonFile, boolean generateCsvFile) {
		this.connectorList = connectorList;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
	}

	public GenerateConnectorEvent(ConnectorList connectorList, boolean generateJsonFile, boolean generateCsvFile, String fileName) {
		this.connectorList = connectorList;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
		this.fileName = fileName;
	}

	public GenerateConnectorEvent() {}

}
