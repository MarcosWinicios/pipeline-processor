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

@Component
public class ExcelUtil {
	
	
	 
	 
	 public static void generateExcelFile(String[] headers, String[][] data, String diretorio, String nomeArquivo) throws IOException {
	        // Criando um diretório se não existir
	        File directory = new File(diretorio);
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
	        File file = new File(directory, nomeArquivo);
	        try (FileOutputStream fileOut = new FileOutputStream(file)) {
	            workbook.write(fileOut);
	            System.out.println("Arquivo Excel gerado com sucesso em: " + file.getAbsolutePath());
	        }

	        // Fechando o workbook
	        workbook.close();
	    }
}
