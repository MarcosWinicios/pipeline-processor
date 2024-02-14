package com.marcos.processor.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Connector {
	
	private String name;
	private List<RequestAPI> requestList;
	
}
