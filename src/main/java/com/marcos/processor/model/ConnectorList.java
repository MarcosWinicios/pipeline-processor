package com.marcos.processor.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectorList {
	
	private String title;
	
	private List<Connector> connectors;
}
