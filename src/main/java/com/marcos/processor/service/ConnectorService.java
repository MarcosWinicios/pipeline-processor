package com.marcos.processor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.marcos.processor.dto.input.GenerateConnectorListInputDTO;
import com.marcos.processor.event.GenerateConnectorEvent;
import com.marcos.processor.listener.GenerateConnectorListener;
import com.marcos.processor.model.Connector;
import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.model.RequestAPI;
import com.marcos.processor.utils.JsonUtil;
import com.marcos.processor.utils.PipelineUtil;

@Service
public class ConnectorService {

	private static final String PATH_FILE = "src/main/resources/connectors/v3/TCOR-V3-ACCOUNT-LIST.json";

//	private static final String PATH_DIRECTORY = "src/main/resources/connectors/v3/influx";

	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	
	public Connector extractConnectorOfFile() {
		try {
			return this.extractConnectorOfFile(PATH_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	public Connector extractConnectorOfFile(String path) {
		try {
			JsonObject pipeline = JsonUtil.fileToJson(path);

			JsonArray steps = PipelineUtil.extractSteps(pipeline);

			List<RequestAPI> endpoints = PipelineUtil.extractEndPointsOfConnectors(steps);

			String connectorName = pipeline.get("name").getAsString();

			Connector connector = new Connector(connectorName, endpoints);

			return connector;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	public Connector extractConnectorOfPipelineJson(JsonObject pipeline) {
		try {
			JsonArray steps = PipelineUtil.extractSteps(pipeline);

			List<RequestAPI> endpoints = PipelineUtil.extractEndPointsOfConnectors(steps);

			String connectorName = pipeline.get("name").getAsString().toString();

			Connector connector = new Connector(connectorName, endpoints);

			return connector;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	public String getConnector() {
		Connector connector = this.extractConnectorOfFile();
		String response = JsonUtil.toJson(connector);
		return response;
	}

	public String getConnector(String path) {
		Connector connector = this.extractConnectorOfFile(path);
		String response = JsonUtil.toJson(connector);
		return response;
	}


	public ConnectorList getConnectorList(GenerateConnectorListInputDTO inputData) {
		List<JsonObject> pipelineList = JsonUtil.getJsonPipelinesList(inputData.getOriginDirectoryPathFiles());
		
		List<Connector> connectorList = new ArrayList<>();

		for (JsonObject pipeline : pipelineList) {
			Connector connector = this.extractConnectorOfPipelineJson(pipeline);
			connectorList.add(connector);
		}
		//---------
		String lastdirectoryName = JsonUtil.getLastPathDirectoryName();

		String connectorsName = "";

		if (lastdirectoryName.equals("influx")) {
			connectorsName = JsonUtil.getPipelinesDirectoryName();
		} else {
			connectorsName = lastdirectoryName;
		}
		
		//---------
		
		ConnectorList connectors = new ConnectorList(connectorsName, connectorList);
		
		
		this.generateFiles(
				connectors, 
				inputData.isGenerateJsonFile(), 
				inputData.isGenerateCsvFile(),
				inputData.getJsonTargetPath(),
				inputData.getCsvTargetPath(),
				inputData.isGenerateCsvWithConnectorsName(),
				inputData.getFileName()
			);
		
		return connectors;
	}
	
	private void generateFiles(ConnectorList connectorList, boolean generateJsonFile, boolean generateCsvFile, 
			String jsonPathTargetFile, String csvPathTargetFile, boolean generateCsvWithConnectorsName, String filename) {

		GenerateConnectorEvent event = new GenerateConnectorEvent(connectorList, generateJsonFile, generateCsvFile);
		
		
		if(jsonPathTargetFile != null) {
			event.setJsonTargetPath(jsonPathTargetFile);
		}
		
		if(csvPathTargetFile != null) {
			event.setCsvTargetPath(csvPathTargetFile);
		}
		
		if(generateCsvFile) {
			event.setGenerateCsvWithConnectorsName(generateCsvWithConnectorsName);
		}
		
		if(filename != null) {
			event.setFileName(filename);
		}
		
		GenerateConnectorListener listener = new GenerateConnectorListener();
		listener.generateConnectorJsonFileListener(event);
		listener.generateConnectorCsvFileListener(event);
		
//		eventPublisher.publishEvent(event);
	}
}
