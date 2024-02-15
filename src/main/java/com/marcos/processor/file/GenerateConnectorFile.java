package com.marcos.processor.file;

import org.springframework.stereotype.Component;

import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.utils.CsvUtil;
import com.marcos.processor.utils.ExcelUtil;
import com.marcos.processor.utils.JsonUtil;

@Component
public class GenerateConnectorFile {
	
	private static final String DEFAULT_PATH_OUTPUT_JSON = "./outputFiles/jsonFiles/";
	private static final String DEFAULT_PATH_OUTPUT_CSV = "./outputFiles/csvFiles/";
	private static final String DEFAULT_PATH_OUTPUT_EXCEL = "./outputFiles/excelFiles/";
	private static final String DEFAULT_OUTPUT_PATH_FILES = "./outputFiles/";

	/**
	 * 
	 * @param connectorList
	 */
	public void generateJsonFile(ConnectorList connectorList) {
		this.generateJsonFile(connectorList, DEFAULT_PATH_OUTPUT_JSON, connectorList.getName());
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param filename
	 */
	public void generateJsonFile(ConnectorList connectorList, String filename) {
		this.generateJsonFile(connectorList, DEFAULT_PATH_OUTPUT_JSON, filename);
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param path
	 * @param filename
	 */
	public void generateJsonFile(ConnectorList connectorList, String path, String filename) {
		JsonUtil.jsonToFile(connectorList, path, filename + ".json");
	}
	
	
	/**
	 * O nome do arquivo gerado será definido pelo retorno do método getName() do
	 * objeto do tipo {@link ConnectorList} informado no argumento.<br>
	 * <br>
	 * 
	 * O arquivo será salvo no diretório padrão
	 *
	 * 
	 * @param connectorList Objeto {@link ConnectorList} que representa uma lista de connectores
	 * @param generateConnectorNameColumn Define se uma coluna com o nome dos connectores será adicionada ao arquivo
	 */
	public void generateCsvFile(ConnectorList connectorList, boolean generateConnectorNameColumn) {
		this.generateCsvFile(connectorList, DEFAULT_PATH_OUTPUT_CSV, generateConnectorNameColumn, connectorList.getName());
	}
	

	
	/**
	 * 
	 * @param connectorList Objeto {@link ConnectorList} que representa uma lista de connectores
	 * @param generateConnectorNameColumn Define se uma coluna com o nome dos connectores será adicionada ao arquivo
	 * @param filename Nome do arquivo alvo a ser gerado
	 */
	public void generateCsvFile(ConnectorList connectorList, boolean generateConnectorNameColumn, String filename) {
		this.generateCsvFile(connectorList, DEFAULT_PATH_OUTPUT_CSV, generateConnectorNameColumn, filename);
	}
	
	
	/**
	 * 
	 * @param connectorList
	 * @param path
	 * @param generateConnectorNameColumn
	 */
	public void generateCsvFile(ConnectorList connectorList, String path,  boolean generateConnectorNameColumn) {
		this.generateCsvFile(connectorList, path, generateConnectorNameColumn, connectorList.getName());
	}
	

	/**
	 * 
	 * @param connectorList Objeto {@link ConnectorList} que representa uma lista de connectores
	 * @param path Diretório alvo onde o arquivo será salvo
	 * @param filename  Nome do arquivo a ser gerado
	 * @param generateConnectorNameColumn Define se uma coluna com o nome dos connectores será adicionada ao arquivo
	 */
	public void generateCsvFile(ConnectorList connectorList, String path, boolean generateConnectorNameColumn, String filename) {
		CsvUtil.connectorListToCsv(connectorList, path, filename, generateConnectorNameColumn);
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param generateConnectorNameColumn
	 */
	public void generateExcelFile(ConnectorList connectorList, boolean generateConnectorNameColumn) {
		generateExcelFile(connectorList, DEFAULT_PATH_OUTPUT_EXCEL, generateConnectorNameColumn, connectorList.getName());
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param targetPathDirectory
	 * @param generateConnectorNameColumn
	 */
	public void generateExcelFile(ConnectorList connectorList, String targetPathDirectory, boolean generateConnectorNameColumn) {
		generateExcelFile(connectorList, targetPathDirectory, generateConnectorNameColumn, connectorList.getName());
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param generateConnectorNameColumn
	 * @param fileName
	 */
	public void generateExcelFile(ConnectorList connectorList, boolean generateConnectorNameColumn, String fileName) {	
		generateExcelFile(connectorList, DEFAULT_PATH_OUTPUT_EXCEL, generateConnectorNameColumn, fileName);
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param targetPathDirectory
	 * @param generateConnectorNameColumn
	 * @param fileName
	 */
	public void generateExcelFile(ConnectorList connectorList, String targetPathDirectory, boolean generateConnectorNameColumn, String fileName) {
		ExcelUtil.connectorListToExcel(connectorList, targetPathDirectory, fileName);
	}
}
