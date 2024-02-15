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

		var connectorList = service.getConnectorList(inputData);

		System.out.println("Terminado");

	}
	
	
	public static void connectorListToExcel(ConnectorList connectorList) {

		String[] headers = { "CONNECTOR", "METHOD", "ENDPOINT" };

		int lines = connectorList.getConnectors().size();
		int columns = headers.length;

		String[][] data = new String[lines][columns];

		System.out.println("linhas: " + lines);
		System.out.println("columns: " + columns);
		System.out.println("Tamanho do data: " + data.length);

		for (int i = 0; i < data.length; i++) {
			var connectorName = "";
			var httpMethod = "";
			var endpoint = "";

			connectorName = connectorList.getConnectors().get(i).getName();
			var size = connectorList.getConnectors().get(i).getRequestList().size();

			if (size > 0) {
				int auxCount = i;
				for (int j = 0; j < size; j++) {
					
					auxCount = auxCount + j;
					httpMethod = connectorList.getConnectors().get(i).getRequestList().get(j).getHttpMethod();
					endpoint = connectorList.getConnectors().get(i).getRequestList().get(j).getEndpoint();
					
					data[auxCount][0] = connectorName;
					data[auxCount][1] = httpMethod;
					data[auxCount][2] = endpoint;
				}
				
				i = auxCount;
			}

			System.out.println(connectorName);
			System.out.println(httpMethod);
			System.out.println(endpoint);

			data[i][0] = connectorName;
			data[i][1] = httpMethod;
			data[i][2] = endpoint;
			System.out.println("Fim do laço: " + i);
		}

		printData(data);
		// Caminho do diretório e nome do arquivo
		String diretorio = "/home/marcos-winicios/Workspaces/tmp";
		String nomeArquivo = "newfile.xlsx";

		// Chamada do método para criar e gravar o arquivo Excel
		try {
			ExcelUtil.generateExcelFile(headers, data, diretorio, nomeArquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void printData(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.println("- " + data[i][j]);
			}
		}
	}

}
