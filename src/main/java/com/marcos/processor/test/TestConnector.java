package com.marcos.processor.test;

import org.springframework.stereotype.Component;

import com.marcos.processor.dto.input.GenerateConnectorListInputDTO;
import com.marcos.processor.service.ConnectorService;
import com.marcos.processor.utils.JsonUtil;

@Component
public class TestConnector {

	public static void main(String[] args) {

		GenerateConnectorListInputDTO inputData;

		ConnectorService service = new ConnectorService();

		String path = "/home/marcos-winicios/Workspaces/topazcore/v3/influx/";

		
		boolean csv = true;
		boolean json = true;
		boolean addConnectorName = true;
		
		
		inputData = new GenerateConnectorListInputDTO(json, csv);
		inputData.setOriginDirectoryPathFiles(path);
		inputData.setGenerateCsvWithConnectorsName(addConnectorName);
		
		var response = JsonUtil.toJson(inputData);
		
		System.out.println(response);
		
		
		service.getConnectorList(inputData);
		

		System.out.println("Terminado");

	}

}
