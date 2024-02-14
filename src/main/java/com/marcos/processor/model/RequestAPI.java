package com.marcos.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestAPI {
	private String httpMethod;
	private String endpoint;
	private String description;
}
