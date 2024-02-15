package com.marcos.processor.file;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.utils.CsvUtil;
import com.marcos.processor.utils.ExcelUtil;
import com.marcos.processor.utils.JsonUtil;

@Component
public class GenerateConnectorFile {
	
	private static final String DEFAULT_FOLDER_OUTPUT_JSON = "json/";
	private static final String DEFAULT_FOLDER_OUTPUT_CSV = "csv/";
	private static final String DEFAULT_FOLDER_OUTPUT_EXCEL = "excel/";
	private static final String DEFAULT_OUTPUT_PATH_FILES = "outputFiles/";
	private static String outputPathUsed = "";
	
	/**
	 * 
	 * @param pathDirectory
	 * @param lastFolderName
	 * @return
	 */
	private String buildPath(String pathDirectory) {
		
		if(!pathDirectory.endsWith("/")) {
			pathDirectory = pathDirectory + "/";
		}
		
		return pathDirectory;
	}

	/**
	 * 
	 * @param connectorList
	 */
	public void generateJsonFile(ConnectorList connectorList) {
		this.generateJsonFile(connectorList, this.buildPath(DEFAULT_OUTPUT_PATH_FILES), connectorList.getName());
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param filename
	 */
	public void generateJsonFile(ConnectorList connectorList, String filename) {
		this.generateJsonFile(connectorList, this.buildPath(DEFAULT_OUTPUT_PATH_FILES), filename);
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param pathDirectory
	 * @param filename
	 */
	public void generateJsonFile(ConnectorList connectorList, String pathDirectory, String filename) {
		
		var path = JsonUtil.jsonToFile(connectorList, pathDirectory + DEFAULT_FOLDER_OUTPUT_JSON, filename + ".json");
		setOutputPathUsed(path);
		
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
		this.generateCsvFile(connectorList, this.buildPath(DEFAULT_OUTPUT_PATH_FILES), generateConnectorNameColumn, connectorList.getName());
	}
	

	
	/**
	 * 
	 * @param connectorList Objeto {@link ConnectorList} que representa uma lista de connectores
	 * @param generateConnectorNameColumn Define se uma coluna com o nome dos connectores será adicionada ao arquivo
	 * @param filename Nome do arquivo alvo a ser gerado
	 */
	public void generateCsvFile(ConnectorList connectorList, boolean generateConnectorNameColumn, String filename) {
		this.generateCsvFile(connectorList, this.buildPath(DEFAULT_OUTPUT_PATH_FILES), generateConnectorNameColumn, filename);
	}
	
	
	/**
	 * 
	 * @param connectorList
	 * @param path
	 * @param generateConnectorNameColumn
	 */
	public void generateCsvFile(ConnectorList connectorList, String path,  boolean generateConnectorNameColumn) {
		this.generateCsvFile(connectorList, this.buildPath(path), generateConnectorNameColumn, connectorList.getName());
	}
	

	/**
	 * 
	 * @param connectorList Objeto {@link ConnectorList} que representa uma lista de connectores
	 * @param pathDirectory Diretório alvo onde o arquivo será salvo
	 * @param filename  Nome do arquivo a ser gerado
	 * @param generateConnectorNameColumn Define se uma coluna com o nome dos connectores será adicionada ao arquivo
	 */
	public void generateCsvFile(ConnectorList connectorList, String pathDirectory, boolean generateConnectorNameColumn, String filename) {
		
		var path = CsvUtil.connectorListToCsv(connectorList, pathDirectory + DEFAULT_FOLDER_OUTPUT_CSV, filename, generateConnectorNameColumn);
		
		setOutputPathUsed(path);
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param generateConnectorNameColumn
	 */
	public void generateExcelFile(ConnectorList connectorList, boolean generateConnectorNameColumn) {
		generateExcelFile(connectorList, this.buildPath(DEFAULT_OUTPUT_PATH_FILES), generateConnectorNameColumn, connectorList.getName());
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param targetPathDirectory
	 * @param generateConnectorNameColumn
	 */
	public void generateExcelFile(ConnectorList connectorList, String targetPathDirectory, boolean generateConnectorNameColumn) {
		generateExcelFile(connectorList, this.buildPath(targetPathDirectory), generateConnectorNameColumn, connectorList.getName());
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param generateConnectorNameColumn
	 * @param fileName
	 */
	public void generateExcelFile(ConnectorList connectorList, boolean generateConnectorNameColumn, String fileName) {	
		generateExcelFile(connectorList, this.buildPath(DEFAULT_OUTPUT_PATH_FILES), generateConnectorNameColumn, fileName);
	}
	
	/**
	 * 
	 * @param connectorList
	 * @param pathDirectory
	 * @param generateConnectorNameColumn
	 * @param fileName
	 */
	public void generateExcelFile(ConnectorList connectorList, String pathDirectory, boolean generateConnectorNameColumn, String fileName) {
		
		var path = ExcelUtil.connectorListToExcel(connectorList, pathDirectory + DEFAULT_FOLDER_OUTPUT_EXCEL, generateConnectorNameColumn, fileName);
		
		setOutputPathUsed(path);
	}

	public static String getOutputPathUsed() {
		return outputPathUsed;
	}

	public static void setOutputPathUsed(String outputPathUsed) {
		
		
		Path pathObject = FileSystems.getDefault().getPath(outputPathUsed);

		String path = pathObject.getParent().getParent().toString();
		
		GenerateConnectorFile.outputPathUsed = path;
	}

}
