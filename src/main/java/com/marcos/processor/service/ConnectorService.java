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
import com.marcos.processor.file.GenerateConnectorFile;
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

			connectorName = PipelineUtil.removePrefixOfConnectorName(connectorName);

			Connector connector = new Connector(connectorName, endpoints);

			return connector;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * 
	 * @return String path em que os arquivos foram salvos
	 */
	public String getPathSavedFiles() {
		var response = GenerateConnectorFile.getOutputPathUsed();

		return response;
	}

	/**
	 * 
	 * @param inputData
	 * @return
	 */
	public ConnectorList buildExportFiles(GenerateConnectorListInputDTO inputData) {
		List<JsonObject> pipelineList = JsonUtil.getJsonPipelinesList(inputData.getOriginDirectoryPathFiles());

		List<Connector> connectorList = new ArrayList<>();

		boolean addEmptyConnectorsInList = false;

		if (inputData.isGroupByConnectorName()) {
			addEmptyConnectorsInList = inputData.isAddEmptyConnectors();
		}
		connectorList = this.pipeListToConnectorList(pipelineList, addEmptyConnectorsInList);

		String connectorsName = PipelineUtil.getConnectorsName(inputData.getOriginDirectoryPathFiles());

//		var ordenedList = this.moveEmptyListsForEnd(connectorList);

		ConnectorList connectors = new ConnectorList(connectorsName, connectorList);

		this.generateFiles(connectors, inputData.isGenerateJsonFile(), inputData.isGenerateCsvFile(),
				inputData.isGenerateExcelFile(), inputData.getOutputPathFiles(), inputData.isGroupByConnectorName(),
				inputData.getFileName());

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

	/**
	 * 
	 * @param connectorList
	 * @param generateJsonFile
	 * @param generateCsvFile
	 * @param generateExcelFile
	 * @param outputPathFile
	 * @param groupByconnectorsName
	 * @param filename
	 */
	private void generateFiles(ConnectorList connectorList, boolean generateJsonFile, boolean generateCsvFile,
			boolean generateExcelFile, String outputPathFile, boolean groupByconnectorsName, String filename) {

		GenerateConnectorEvent event = new GenerateConnectorEvent(connectorList, generateJsonFile, generateCsvFile,
				generateExcelFile);

		event.setGroupByConnectorsName(groupByconnectorsName);

		if (outputPathFile != null) {
			event.setOutputPathFile(outputPathFile);
		}

		if (filename != null) {
			event.setFileName(filename);
		}

		GenerateConnectorListener listener = new GenerateConnectorListener();
		listener.generateConnectorJsonFileListener(event);
		listener.generateConnectorCsvFileListener(event);
		listener.generateConnectorExcelFileListener(event);

//		eventPublisher.publishEvent(event);
	}
}
