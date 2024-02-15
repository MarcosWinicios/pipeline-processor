package com.marcos.processor.event;

import org.springframework.stereotype.Component;

import com.marcos.processor.model.ConnectorList;

import lombok.Data;

@Data
@Component
public class GenerateConnectorEvent {

	private ConnectorList connectorList;
	private boolean groupByConnectorsName = true;
	private boolean generateJsonFile;
	private boolean generateCsvFile;
	private boolean generateExcelFile;
	private String fileName;
	private String outputPathFile;
	
	
	
	public GenerateConnectorEvent(ConnectorList connectorList, boolean generateJsonFile, boolean generateCsvFile,boolean generateExcelFile) {
		this.connectorList = connectorList;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
		this.generateExcelFile = generateExcelFile;
	}

	public GenerateConnectorEvent() {}

}
