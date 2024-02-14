package com.marcos.processor.config;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class FilesProperties {
	
	@Value("${directory.value.json.input}")
	private String jsonInput;
	
	@Value("${directory.value.json.output}")
	private String jsonOutput;

}
