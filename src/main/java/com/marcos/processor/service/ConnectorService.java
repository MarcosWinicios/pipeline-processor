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


	@Autowired
	private ApplicationEventPublisher eventPublisher;

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

	public Connector extractConnectorOfJsonPipeline(JsonObject pipeline) {
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

	public ConnectorList getConnectorList(GenerateConnectorListInputDTO inputData) {
		List<JsonObject> pipelineList = JsonUtil.getJsonPipelinesList(inputData.getOriginDirectoryPathFiles());

		List<Connector> connectorList = new ArrayList<>();

		connectorList = this.pipeListToConnectorList(pipelineList, inputData.isAddEmptyConnectors());

		String connectorsName = PipelineUtil.getConnectorsName(inputData.getOriginDirectoryPathFiles());
		
		var ordenedList = this.moveEmptyListsForEnd(connectorList);

		ConnectorList connectors = new ConnectorList(connectorsName, ordenedList);

		this.generateFiles(connectors, inputData.isGenerateJsonFile(), inputData.isGenerateCsvFile(),
				inputData.getJsonTargetPath(), inputData.getCsvTargetPath(),
				inputData.isGenerateCsvWithConnectorsName(), inputData.getFileName());

		return connectors;
	}


	private List<Connector> pipeListToConnectorList(List<JsonObject> pipelineList, boolean addEmptyRequestList) {
		if (addEmptyRequestList) {
			return this.pipeListToConnectorListWithEmptyRequestList(pipelineList);
		}

		return this.pipeListToConnectorListWithoutEmptyRequestList(pipelineList);
	}

	private List<Connector> pipeListToConnectorListWithoutEmptyRequestList(List<JsonObject> pipelineList) {

		List<Connector> connectorList = new ArrayList<>();

		for (JsonObject pipeline : pipelineList) {
			Connector connector = this.extractConnectorOfJsonPipeline(pipeline);

			if (connector.getRequestList().size() > 0) {
				connectorList.add(connector);
			}
		}
		return connectorList;
	}

	private List<Connector> pipeListToConnectorListWithEmptyRequestList(List<JsonObject> pipelineList) {

		List<Connector> connectorList = new ArrayList<>();

		for (JsonObject pipeline : pipelineList) {
			Connector connector = this.extractConnectorOfJsonPipeline(pipeline);

			connectorList.add(connector);
		}
		return connectorList;
	}

	private List<Connector> moveEmptyListsForEnd(List<Connector> list) {
		var emptyList = new ArrayList<Connector>();
		var responseList = new ArrayList<Connector>();

		for (Connector connector : list) {
			if (connector.getRequestList().size() > 0) {
				responseList.add(connector);
				continue;
			}
			emptyList.add(connector);
		}

		for (Connector connector : emptyList) {
			responseList.add(connector);
		}

		return responseList;
	}

	private void generateFiles(ConnectorList connectorList, boolean generateJsonFile, boolean generateCsvFile,
			String jsonPathTargetFile, String csvPathTargetFile, boolean generateCsvWithConnectorsName,
			String filename) {

		GenerateConnectorEvent event = new GenerateConnectorEvent(connectorList, generateJsonFile, generateCsvFile);

		if (jsonPathTargetFile != null) {
			event.setJsonTargetPath(jsonPathTargetFile);
		}

		if (csvPathTargetFile != null) {
			event.setCsvTargetPath(csvPathTargetFile);
		}

		if (generateCsvFile) {
			event.setGenerateCsvWithConnectorsName(generateCsvWithConnectorsName);
		}

		if (filename != null) {
			event.setFileName(filename);
		}

		GenerateConnectorListener listener = new GenerateConnectorListener();
		listener.generateConnectorJsonFileListener(event);
		listener.generateConnectorCsvFileListener(event);

//		eventPublisher.publishEvent(event);
	}
}
