package com.marcos.processor.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marcos.processor.model.RequestAPI;

public class PipelineUtil {

	private final static List<String> BASE_URL_VARIABLES = new ArrayList<String>(
			Arrays.asList("$V{TSMC_TBNK_SERVER}", "$V{TSMC_TCOR_SERVER}", "$V{TSMC_MOCK_TBNK_SERVER}"));

	private final static List<String> PREFIX_CONNECTORS_NAME = new ArrayList<>(
			Arrays.asList("TCOR-V2-", "TCOR-V3-", "TCORV-V3-", "TCOR-MXV1-"));

	private final static String DEFAULT_LAST_DIRECTORY_NAME = "influx";

	@SuppressWarnings("unused")
	public static JsonArray extractSteps(JsonObject pipelineJson) throws Exception {
		JsonArray steps = pipelineJson.get("steps").getAsJsonArray();

		return steps;

	}

	public static List<RequestAPI> extractEndPointsOfConnectors(JsonArray steps) {

		List<RequestAPI> requestList = new ArrayList<>();

		for (JsonElement stepElement : steps) {
			JsonObject step = stepElement.getAsJsonObject();
			RequestAPI requestApi = extractEndPointOfStep(step);
			if (requestApi != null) {
				requestList.add(requestApi);
			}
		}

		return requestList;
	}

	public static RequestAPI extractEndPointOfStep(JsonObject step) {

		String component = step.get("component").getAsString();

		if (!component.equals("connector-http")) {
			return null;
		}
		String description = step.get("description").getAsString();

		JsonObject config = step.get("config").getAsJsonObject();
		String method = config.get("method").getAsString();
		String endpoint = config.get("endpoint").getAsString();

		for (String baseUrl : BASE_URL_VARIABLES) {
			endpoint = endpoint.replace(baseUrl, "");
		}

		return new RequestAPI(method, endpoint, description);

	}

	public static String getConnectorsName(String path) {
		Path pathObject = FileSystems.getDefault().getPath(path);

		String penultimateDirectoryName = pathObject.getParent().getFileName().toString();

		String lastDirectoryName = pathObject.getFileName().toString();

		if (lastDirectoryName.equals(DEFAULT_LAST_DIRECTORY_NAME)) {
			return penultimateDirectoryName;
		}
		return lastDirectoryName;

	}

	public static String removePrefixOfConnectorName(String connectorName) {		
		for (String baseUrl : PREFIX_CONNECTORS_NAME) {
			connectorName = connectorName.replace(baseUrl, "");
		}
		return connectorName;
	}

}
