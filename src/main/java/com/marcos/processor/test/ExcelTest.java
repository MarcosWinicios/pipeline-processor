package com.marcos.processor.test;

import java.io.IOException;

import com.marcos.processor.utils.ExcelUtil;


public class ExcelTest {
	
	public static void main(String[] args) {
        // Exemplo de dados para adicionar ao arquivo Excel
        String[][] data = {
            {"João", "30", "Brasil", "Engenheiro"},
            {"Maria", "25", "Brasil", "Professora"}
        };

        // Exemplo de cabeçalho
        String[] headers = {"Nome", "Idade", "País", "Profissão"};

        // Caminho do diretório e nome do arquivo
        String diretorio = "/home/marcos-winicios/Workspaces/tmp";
        String nomeArquivo = "newfile.xlsx";

        // Chamada do método para criar e gravar o arquivo Excel
        try {
            ExcelUtil.generateExcelFile(headers, data, diretorio, nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
