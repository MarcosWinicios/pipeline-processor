package com.marcos.processor.test;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.marcos.processor.dto.input.GenerateConnectorListInputDTO;
import com.marcos.processor.file.GenerateConnectorFile;
import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.service.ConnectorService;
import com.marcos.processor.utils.ExcelUtil;
import com.marcos.processor.utils.JsonUtil;

@Component
public class TestConnector {

	public static void main(String[] args) {

		GenerateConnectorListInputDTO inputData;

		ConnectorService service = new ConnectorService();

//		String path = "/home/marcos-winicios/Workspaces/topazcore/v3/influx/";
		String path = "C:\\documents\\topazcore\\v3\\influx\\";
		

		boolean csv = true;
		boolean json = true;
		boolean excel = true;
		
		boolean addConnectorName = true;

		inputData = new GenerateConnectorListInputDTO(json, csv, excel);
		inputData.setOriginDirectoryPathFiles(path);
		inputData.setGroupByConnectorName(addConnectorName);

		var response = JsonUtil.toJson(inputData);
		
		System.out.println("payload" + "\n" + response);

		var connectorList = service.buildExportFiles(inputData);

		System.out.println("Terminado");

	}


	public static void printData(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.println("- " + data[i][j]);
			}
		}
	}

}
