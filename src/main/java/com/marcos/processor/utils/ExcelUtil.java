package com.marcos.processor.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.marcos.processor.model.ConnectorList;

@Component
public class ExcelUtil {
	private static final String[] HEADERS_GROUP_BY_CONNECTORS_NAME = {"CONNECTOR", "METHOD", "ENDPOINT" };
	private static final String[] HEADERS_WITHOUT_CONNECTORS_NAME = {"METHOD", "ENDPOINT" };

	/**
	 * 
	 * @param connectorList
	 * @param pathDirectory
	 * @param groupByConnectorsName
	 * @param filename
	 * @throws IOException 
	 */
	public static String connectorListToExcel(ConnectorList connectorList, String pathDirectory, boolean groupByConnectorsName, String filename)  {
		
		String[] headers = null;
		String[][] data = null;

		
		if(groupByConnectorsName) {
			headers = HEADERS_GROUP_BY_CONNECTORS_NAME;
			data = generateDateWithGroupingByConnectorName(connectorList);
		}else {
			headers = HEADERS_WITHOUT_CONNECTORS_NAME;
			data = generateDateWithoutGroupingByConnectorName(connectorList);
		}
		
		
		// Chamada do método para criar e gravar o arquivo Excel
		try {
			
			return ExcelUtil.generateExcelFile(headers, data, pathDirectory, filename);
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}
	
	private static String[][] generateDateWithGroupingByConnectorName(ConnectorList connectorList){
		

		int lines = connectorList.getConnectors().size();
		int columns = HEADERS_GROUP_BY_CONNECTORS_NAME.length;

		String[][] data = new String[lines][columns];

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

			data[i][0] = connectorName;
			data[i][1] = httpMethod;
			data[i][2] = endpoint;
		}

		printData(data);

		
		
		return data;
	}
	
private static String[][] generateDateWithoutGroupingByConnectorName(ConnectorList connectorList){
		

		int lines = connectorList.getConnectors().size();
		int columns = HEADERS_WITHOUT_CONNECTORS_NAME.length;

		String[][] data = new String[lines][columns];

		for (int i = 0; i < data.length; i++) {
			var httpMethod = "";
			var endpoint = "";

			var size = connectorList.getConnectors().get(i).getRequestList().size();

			if (size > 0) {
				int auxCount = i;
				for (int j = 0; j < size; j++) {

					auxCount = auxCount + j;
					httpMethod = connectorList.getConnectors().get(i).getRequestList().get(j).getHttpMethod();
					endpoint = connectorList.getConnectors().get(i).getRequestList().get(j).getEndpoint();
					
					data[auxCount][0] = httpMethod;
					data[auxCount][1] = endpoint;
				}

				i = auxCount;
			}

			data[i][0] = httpMethod;
			data[i][1] = endpoint;
		}

//		printData(data);
		
		return data;
	}
 

	public static String generateExcelFile(String[] headers, String[][] data, String pathDirectory, String fileName)
			throws IOException {
		// Criando um diretório se não existir
		File directory = new File(pathDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Criando um novo livro de trabalho (workbook)
		Workbook workbook = new XSSFWorkbook();

		// Criando uma nova planilha (sheet)
		Sheet sheet = workbook.createSheet("Planilha1");

		// Criando uma linha na planilha para o cabeçalho
		Row headerRow = sheet.createRow(0);

		// Adicionando cabeçalhos às colunas
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Adicionando os dados à planilha
		for (int i = 0; i < data.length; i++) {
			Row row = sheet.createRow(i + 1);
			for (int j = 0; j < data[i].length; j++) {
				row.createCell(j).setCellValue(data[i][j]);
			}
		}
		
		// Salvando o arquivo Excel
		File file = new File(directory, fileName + ".xlsx");
		try (FileOutputStream fileOut = new FileOutputStream(file)) {
			workbook.write(fileOut);
			System.out.println("Arquivo Excel gerado com sucesso em: " + file.getAbsolutePath());
		}

		// Fechando o workbook
		workbook.close();
		return file.getAbsolutePath();
	}

	public static void printData(String[][] data) {
		System.out.println("\n\nPRINTANDO DADOS\n\n");
		for (int i = 0; i < data.length; i++) {

			for (int j = 0; j < data[i].length; j++) {
				
				if(j == (data[i].length -1)) {	
					System.out.println(data[i][j]);
					continue;
				}
				System.out.print(data[i][j] + " | ");
				
			}
		}
		System.out.println("\n\nTERMINANDO DE PRINTAR DADOS\n\n");
	}
}
