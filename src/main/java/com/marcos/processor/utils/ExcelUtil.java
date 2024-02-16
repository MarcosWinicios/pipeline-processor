package com.marcos.processor.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.marcos.processor.model.ConnectorList;

@Component
public class ExcelUtil {
	private static final String[] HEADERS_GROUP_BY_CONNECTORS_NAME = { "CONNECTOR", "METHOD", "ENDPOINT" };
	private static final String[] HEADERS_WITHOUT_CONNECTORS_NAME = { "METHOD", "ENDPOINT" };

	/**
	 * 
	 * @param connectorList
	 * @param pathDirectory
	 * @param groupByConnectorsName
	 * @param filename
	 * @throws IOException
	 */
	public static String connectorListToExcel(ConnectorList connectorList, String pathDirectory,
			boolean groupByConnectorsName, String filename) {

		String[] headers = null;
		String[][] data = null;

		if (groupByConnectorsName) {
			headers = new String[3];

			headers[0] = connectorList.getTitle().toUpperCase();
			headers[1] = connectorList.getTitle().toUpperCase();
			headers[2] = connectorList.getTitle().toUpperCase();

			data = generateDateWithGroupingByConnectorName(connectorList);
		} else {
			headers = new String[2];
			headers[0] = connectorList.getTitle().toUpperCase();
			headers[1] = connectorList.getTitle().toUpperCase();

			data = generateDateWithoutGroupingByConnectorName(connectorList);
		}

		// Chamada do método para criar e gravar o arquivo Excel
		try {

			return ExcelUtil.generateExcelFile(headers, data, pathDirectory, filename);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private static String[][] generateDateWithGroupingByConnectorName(ConnectorList connectorList) {

		int columns = HEADERS_GROUP_BY_CONNECTORS_NAME.length;

		String[][] data = null;

		List<String[]> dataList = new ArrayList<>();

		for (int i = 0; i < connectorList.getConnectors().size(); i++) {
			String[] line = new String[columns];

			var connectorName = "";
			var httpMethod = "";
			var endpoint = "";

			if (i == 0) {
				connectorName = HEADERS_GROUP_BY_CONNECTORS_NAME[0];
				httpMethod = HEADERS_GROUP_BY_CONNECTORS_NAME[1];
				endpoint = HEADERS_GROUP_BY_CONNECTORS_NAME[2];

				line[0] = connectorName;
				line[1] = httpMethod;
				line[2] = endpoint;

				dataList.add(line);

				continue;
			}

			connectorName = connectorList.getConnectors().get(i).getName();

			var size = connectorList.getConnectors().get(i).getRequestList().size();

			if (size > 0) {
				int auxCount = i;
				for (int j = 0; j < size; j++) {

					auxCount = auxCount + j;
					httpMethod = connectorList.getConnectors().get(i).getRequestList().get(j).getHttpMethod();
					endpoint = connectorList.getConnectors().get(i).getRequestList().get(j).getEndpoint();

					line[0] = connectorName;
					line[1] = httpMethod;
					line[2] = endpoint;
					dataList.add(line);
				}

				i = auxCount;
				continue;
			}

			line[0] = connectorName;
			line[1] = httpMethod;
			line[2] = endpoint;
			dataList.add(line);
		}

		data = listToArray(dataList);

		return data;
	}

	private static String[][] listToArray(List<String[]> list) {
		String[][] response = null;
		int columns = list.get(0).length;
		int lines = list.size();

		response = new String[lines][columns];

		for (int i = 0; i < list.size(); i++) {
			if (columns == 3) {
				response[i][0] = list.get(i)[0];
				response[i][1] = list.get(i)[1];
				response[i][2] = list.get(i)[2];
			} else if(columns == 2) {
				response[i][0] = list.get(i)[0];
				response[i][1] = list.get(i)[1];
			}
		}

		return response;
	}

	private static String[][] generateDateWithoutGroupingByConnectorName(ConnectorList connectorList) {

		int columns = HEADERS_WITHOUT_CONNECTORS_NAME.length;

		String[][] data = null;

		List<String[]> dataList = new ArrayList<>();

		for (int i = 0; i < connectorList.getConnectors().size(); i++) {
			String[] line = new String[columns];

			var httpMethod = "";
			var endpoint = "";

			var size = connectorList.getConnectors().get(i).getRequestList().size();
			
			if(i == 0) {
				line[0] = HEADERS_WITHOUT_CONNECTORS_NAME[0];
				line[1] = HEADERS_WITHOUT_CONNECTORS_NAME[1];
				
				dataList.add(line);
				
				continue;
			}

			if (size > 0) {
				int auxCount = i;
				for (int j = 0; j < size; j++) {

					auxCount = auxCount + j;
					httpMethod = connectorList.getConnectors().get(i).getRequestList().get(j).getHttpMethod();
					endpoint = connectorList.getConnectors().get(i).getRequestList().get(j).getEndpoint();

					line[0] = httpMethod;
					line[1] = endpoint;
					dataList.add(line);
				}

				i = auxCount;
			}

			line[0] = httpMethod;
			line[1] = endpoint;
			dataList.add(line);
		}

		data = listToArray(dataList);

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

				if (j == (data[i].length - 1)) {
					System.out.println(data[i][j]);
					continue;
				}
				System.out.print(data[i][j] + " | ");

			}
		}
		System.out.println("\n\nTERMINANDO DE PRINTAR DADOS\n\n");
	}
}
