package com.marcos.processor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectorList {
	
	private String title;
	
	private List<Connector> connectors;

	public List<RequestAPI> extractNoDuplicateRequestAPIList(){

		return this.extractRequestAPIList().stream()
				.distinct()
				.toList();
	}

	private List<RequestAPI> extractRequestAPIList(){
		List<RequestAPI> result = new ArrayList<>();

		connectors.forEach((connector) -> {
			result.addAll(connector.getRequestList());
		});

		return result;
	}
}
