package com.marcos.processor.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.marcos.processor.event.GenerateConnectorEvent;
import com.marcos.processor.file.GenerateConnectorFile;


@Component
public class GenerateConnectorListener {
	
	@Autowired
	private GenerateConnectorFile generatorConnectorFile;
	
	public GenerateConnectorListener() {
		this.generatorConnectorFile = new GenerateConnectorFile();
	}
	
	@EventListener
	public void generateConnectorJsonFileListener (GenerateConnectorEvent event) {
		if(!event.isGenerateJsonFile()) {
			return;
		}
		
		if(event.getJsonTargetPath() == null && event.getFileName() == null) {
			generatorConnectorFile.generateJsonFile(event.getConnectorList());
			return;
		}
		
		if(event.getJsonTargetPath() == null && event.getFileName() != null) {
			generatorConnectorFile.generateJsonFile(event.getConnectorList(), event.getFileName());
			return;
		}
		
		generatorConnectorFile.generateJsonFile(event.getConnectorList(), event.getJsonTargetPath(), event.getFileName());
	}
	
	@EventListener
	public void generateConnectorCsvFileListener(GenerateConnectorEvent event) {
		if(!event.isGenerateCsvFile()) {
			return;
		}
		
		//path e filename padrões
		if(event.getCsvTargetPath() == null && event.getFileName() == null) {
			generatorConnectorFile.generateCsvFile(
					event.getConnectorList(), 
					event.isGenerateCsvWithConnectorsName());
			return;
		}
		
		//filename padrão
		if(event.getCsvTargetPath() != null && event.getFileName() == null) {
			generatorConnectorFile.generateCsvFile(
					event.getConnectorList(), 
					event.getCsvTargetPath(), 
					event.isGenerateCsvWithConnectorsName());
			return;
		}
		
		//path padrão 
		if(event.getCsvTargetPath() == null && event.getFileName() != null) {
			generatorConnectorFile.generateCsvFile(
					event.getConnectorList(), 
					event.isGenerateCsvWithConnectorsName(),
					event.getFileName()
					);
			return;
		}
		
	
		//path e filenames customizados
		generatorConnectorFile.generateCsvFile(
				event.getConnectorList(), 
				event.getCsvTargetPath(), 
				event.isGenerateCsvWithConnectorsName(),
				event.getFileName() 
				);
	}
}
