package com.marcos.processor.dto.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateConnectorListInputDTO {

	private String originDirectoryPathFiles;
	private boolean generateCsvWithConnectorsName;
	private boolean generateJsonFile;
	private boolean generateCsvFile;
	private String fileName;
	private String jsonTargetPath;
	private String csvTargetPath;

	/**
	 * 
	 * @param originDirectoryPathFiles
	 * @param generateCsvWithConnectorsName
	 * @param generateJsonFile
	 * @param generateCsvFile
	 * @param fileName
	 * @param jsonTargetPath
	 * @param csvTargetPath
	 */
	public GenerateConnectorListInputDTO(String originDirectoryPathFiles, boolean generateCsvWithConnectorsName,
			boolean generateJsonFile, boolean generateCsvFile, String fileName, String jsonTargetPath,
			String csvTargetPath) {
		this.originDirectoryPathFiles = originDirectoryPathFiles;
		this.generateCsvWithConnectorsName = generateCsvWithConnectorsName;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
		this.fileName = fileName;
		this.jsonTargetPath = jsonTargetPath;
		this.csvTargetPath = csvTargetPath;
	}
	
	/**
	 * 
	 * @param generateJsonFile
	 * @param generateCsvFile
	 */
	public GenerateConnectorListInputDTO(boolean generateJsonFile, boolean generateCsvFile) {
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
	}
	
	/**
	 * 
	 * @param originDirectoryPathFiles
	 * @param generateCsvWithConnectorsName
	 * @param generateJsonFile
	 * @param generateCsvFile
	 */
	public GenerateConnectorListInputDTO(String originDirectoryPathFiles, boolean generateCsvWithConnectorsName,
			boolean generateJsonFile, boolean generateCsvFile) {
		super();
		this.originDirectoryPathFiles = originDirectoryPathFiles;
		this.generateCsvWithConnectorsName = generateCsvWithConnectorsName;
		this.generateJsonFile = generateJsonFile;
		this.generateCsvFile = generateCsvFile;
	}

}
