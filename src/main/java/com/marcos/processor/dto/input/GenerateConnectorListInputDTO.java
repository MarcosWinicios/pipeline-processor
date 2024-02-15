package com.marcos.processor.dto.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateConnectorListInputDTO {

	private String originDirectoryPathFiles;
	private boolean groupByConnectorName;
	private boolean generateJsonFile;
	private boolean generateCsvFile;
	private boolean generateExcelFile;
	private String fileName;
	private String outputPathFiles;
	private boolean addEmptyConnectors = true;



	
	
	
	
/**
 * 
 * @param generateJsonFile
 * @param generateCsvFile
 * @param generateExcelFile
 */
	public GenerateConnectorListInputDTO(boolean generateJsonFile, boolean generateCsvFile, boolean generateExcelFile) {
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
		this.generateExcelFile = generateExcelFile;
	}
	
	/**
	 * 
	 * @param originDirectoryPathFiles
	 * @param groupByConnectorName
	 * @param generateJsonFile
	 * @param generateCsvFile
	 */
	public GenerateConnectorListInputDTO(String originDirectoryPathFiles, boolean groupByConnectorName,
			boolean generateJsonFile, boolean generateCsvFile) {
		super();
		this.originDirectoryPathFiles = originDirectoryPathFiles;
		this.groupByConnectorName = groupByConnectorName;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
	}

	
	/**
	 * 
	 * @param originDirectoryPathFiles
	 * @param groupByConnectorName
	 * @param generateJsonFile
	 * @param generateCsvFile
	 * @param generateExcelFile
	 * @param fileName
	 * @param outputPathFiles
	 * @param addEmptyConnectors
	 */
	public GenerateConnectorListInputDTO(String originDirectoryPathFiles, boolean groupByConnectorName,
			boolean generateJsonFile, boolean generateCsvFile, boolean generateExcelFile, String fileName,
			String outputPathFiles, boolean addEmptyConnectors) {
		super();
		this.originDirectoryPathFiles = originDirectoryPathFiles;
		this.groupByConnectorName = groupByConnectorName;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
		this.generateExcelFile = generateExcelFile;
		this.fileName = fileName;
		this.outputPathFiles = outputPathFiles;
		this.addEmptyConnectors = addEmptyConnectors;
	}

}
