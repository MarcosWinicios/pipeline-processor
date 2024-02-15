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
		
		if(event.getOutputPathFile() == null && event.getFileName() == null) {
			generatorConnectorFile.generateJsonFile(event.getConnectorList());
			return;
		}
		
		if(event.getOutputPathFile() == null && event.getFileName() != null) {
			generatorConnectorFile.generateJsonFile(event.getConnectorList(), event.getFileName());
			return;
		}
		
		generatorConnectorFile.generateJsonFile(event.getConnectorList(), event.getOutputPathFile(), event.getFileName());
	}
	
	@EventListener
	public void generateConnectorCsvFileListener(GenerateConnectorEvent event) {
		if(!event.isGenerateCsvFile()) {
			return;
		}
		
		//path e filename padrões
		if(event.getOutputPathFile() == null && event.getFileName() == null) {
			generatorConnectorFile.generateCsvFile(
					event.getConnectorList(), 
					event.isGroupByConnectorsName());
			return;
		}
		
		//filename padrão
		if(event.getOutputPathFile() != null && event.getFileName() == null) {
			generatorConnectorFile.generateCsvFile(
					event.getConnectorList(), 
					event.getOutputPathFile(), 
					event.isGroupByConnectorsName());
			return;
		}
		
		//path padrão 
		if(event.getOutputPathFile() == null && event.getFileName() != null) {
			generatorConnectorFile.generateCsvFile(
					event.getConnectorList(), 
					event.isGroupByConnectorsName(),
					event.getFileName()
					);
			return;
		}
		
	
		//path e filename customizados
		generatorConnectorFile.generateCsvFile(
				event.getConnectorList(), 
				event.getOutputPathFile(), 
				event.isGroupByConnectorsName(),
				event.getFileName() 
				);
	}
	
	@EventListener
	public void generateConnectorExcelFileListener(GenerateConnectorEvent event) {
		if(!event.isGenerateExcelFile()) {
			return;
		}
	
		//path e filename padrões
				if(event.getOutputPathFile() == null && event.getFileName() == null) {
					generatorConnectorFile.generateExcelFile(
							event.getConnectorList(), 
							event.isGroupByConnectorsName());
					return;
				}
				
				//filename padrão
				if(event.getOutputPathFile() != null && event.getFileName() == null) {
					generatorConnectorFile.generateExcelFile(
							event.getConnectorList(), 
							event.getOutputPathFile(), 
							event.isGroupByConnectorsName());
					return;
				}
				
				//path padrão 
				if(event.getOutputPathFile() == null && event.getFileName() != null) {
					
					generatorConnectorFile.generateExcelFile(
							event.getConnectorList(), 
							event.isGroupByConnectorsName(),
							event.getFileName()
							);
					return;
				}
		
		
		//path e fileName customizados
		this.generatorConnectorFile.generateExcelFile(
				event.getConnectorList(), 
				event.getOutputPathFile(), 
				event.isGroupByConnectorsName(), 
				event.getFileName());
	}
	
	
	
}
