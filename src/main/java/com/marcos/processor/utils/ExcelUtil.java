package com.marcos.processor.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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

			return ExcelUtil.generateExcelFile(headers, data, pathDirectory, filename, connectorList.getTitle());

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	private static String[][] generateDateWithGroupingByConnectorName(ConnectorList connectorList) {

		int columns = HEADERS_GROUP_BY_CONNECTORS_NAME.length;

		String[][] data = null;

		List<String[]> dataList = new ArrayList<>();

		String[] firstLine = { HEADERS_GROUP_BY_CONNECTORS_NAME[0], HEADERS_GROUP_BY_CONNECTORS_NAME[1],
				HEADERS_GROUP_BY_CONNECTORS_NAME[2] };
		dataList.add(firstLine);

		for (int i = 0; i < connectorList.getConnectors().size(); i++) {
			String[] line = new String[columns];

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
			} else if (columns == 2) {
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

			if (i == 0) {
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

	private static String generateExcelFile(String[] headers, String[][] data, String pathDirectory, String fileName,
			String sheetName) throws IOException {

		// Criando um diretório se não existir
		File directory = new File(pathDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Criando um novo livro de trabalho (workbook)
		Workbook workbook = new XSSFWorkbook();

		// *****************
		CellStyle headerStyle = createHeaderStyle(workbook);

		CellStyle borderStyle = createBorderStyle(workbook);

//		CellStyle columnMethoStyle = createColumnMethodStyle(workbook);

		CellStyle stripeStyle = createStripeStyle(workbook);

		// Criando uma nova planilha (sheet)
		Sheet sheet = workbook.createSheet(sheetName);

		// Criando uma linha na planilha para o cabeçalho
		Row headerRow = sheet.createRow(0);

		// Adicionando cabeçalhos às colunas
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle); // Aplicando o estilo centralizado e em negrito
		}

		// Adicionando os dados à planilha
		for (int i = 0; i < data.length; i++) {
			Row row = sheet.createRow(i + 1);
			for (int j = 0; j < data[i].length; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(data[i][j]);

				if (i == 0) {
					cell.setCellStyle(headerStyle); // Aplicando o estilo centralizado e em negrito
				}

				if (i > 0) {
					cell.setCellStyle(borderStyle);

					if (data[i].length == 3) {
						if (j == 1) {
							cell.setCellStyle(createColumnMethodStyle(workbook, data[i][j]));
						}
					}

					if (data[i].length == 2) {
						if (j == 0) {
							cell.setCellStyle(createColumnMethodStyle(workbook, data[i][j]));
						}
					}

				}

			}
		}

		// Mescla da primeira célula até a última do cabeçalho
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

		// Ajustando a largura das colunas para que o conteúdo seja exibido
		// adequadamente
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Salvando o arquivo Excel
		File file = new File(directory, fileName + ".xlsx");
		try (FileOutputStream fileOut = new FileOutputStream(file)) {
			workbook.write(fileOut);
			System.err.println("\nArquivo Excel gerado com sucesso em: " + file.getAbsolutePath() + "\n");
		}

		// Fechando o workbook
		workbook.close();
		return file.getAbsolutePath();
	}

	// Método para criar o estilo de célula de cabeçalho
	private static CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		return headerStyle;
	}

	// Método para criar o estilo de célula de borda
	private static CellStyle createBorderStyle(Workbook workbook) {
		CellStyle borderStyle = workbook.createCellStyle();
		borderStyle.setBorderTop(BorderStyle.THIN);
		borderStyle.setBorderBottom(BorderStyle.THIN);
		borderStyle.setBorderLeft(BorderStyle.THIN);
		borderStyle.setBorderRight(BorderStyle.THIN);
		return borderStyle;
	}

	// Método para criar o estilo de célula de método
	private static CellStyle createColumnMethodStyle(Workbook workbook, String method) {
		CellStyle columnMethodStyle = workbook.createCellStyle();
		columnMethodStyle.setAlignment(HorizontalAlignment.CENTER);
		columnMethodStyle.setBorderTop(BorderStyle.THIN);
		columnMethodStyle.setBorderBottom(BorderStyle.THIN);
		columnMethodStyle.setBorderLeft(BorderStyle.THIN);
		columnMethodStyle.setBorderRight(BorderStyle.THIN);

		Font font = workbook.createFont();

		if (method.equals("POST")) {
			font.setColor(IndexedColors.DARK_TEAL.getIndex());
			font.setBold(true);
			columnMethodStyle.setFont(font);
		}

		if (method.equals("GET")) {
			font.setColor(IndexedColors.GREEN.getIndex());
			font.setBold(true);
			columnMethodStyle.setFont(font);
		}

		return columnMethodStyle;
	}

	// Método para criar o estilo de célula de método
	private static CellStyle createStripeStyle(Workbook workbook) {
		CellStyle stripeStyle = workbook.createCellStyle();

		stripeStyle.setBorderTop(BorderStyle.THIN);
		stripeStyle.setBorderBottom(BorderStyle.THIN);
		stripeStyle.setBorderLeft(BorderStyle.THIN);
		stripeStyle.setBorderRight(BorderStyle.THIN);
		stripeStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());

		return stripeStyle;
	}

	/**
	 * 
	 * @param data {@link String[][]}
	 */
	public static void printData(String[][] data) {
		System.out.println("-----");

		System.out.println("\n\nPRINTANDO DADOS\n\n");
		for (int i = 0; i < data.length; i++) {

			for (int j = 0; j < data[i].length; j++) {

				if (j == (data[i].length - 1)) {
					var value = data[i][j];
					System.out.println(value);
					continue;
				}
				var value = data[i][j];
				System.out.print(value + " | ");

			}
		}
		System.out.println("\n\nTERMINANDO DE PRINTAR DADOS\n\n");
	}

	/**
	 * 
	 * @param data {@link List:String[]}
	 */
	public static void printData(List<String[]> data) {
		System.out.println("-----");

		System.out.println("\n\nPRINTANDO DADOS DA LISTA\n\n");

		data.forEach(x -> {
			System.out.print(x[0] + " | " + x[1] + " | " + x[2] + " | \n");
		});

		System.out.println("\n\nTERMINANDO DE PRINTAR DADOS DA LISTA\n\n");
	}
}
