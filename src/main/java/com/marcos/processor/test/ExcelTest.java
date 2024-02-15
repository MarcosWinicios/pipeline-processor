package com.marcos.processor.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTest {
	
	private static final String DEFAULT_PATH_OUTPUT_EXCEL = "./outputFiles/excelFiles/";
	
    public static void main(String[] args) {
        // Exemplo de dados para adicionar ao arquivo Excel
        ArrayList<Objeto> listaObjetos = new ArrayList<>();
        listaObjetos.add(new Objeto("Objeto1", new ArrayList<>(List.of("String1", "String2", "String3"))));
        listaObjetos.add(new Objeto("Objeto2", new ArrayList<>(List.of("String4"))));
        listaObjetos.add(new Objeto("Objeto3", new ArrayList<>(List.of("String5", "String6"))));
        listaObjetos.add(new Objeto("Objeto4", new ArrayList<>(List.of("String7", "String8", "String9"))));

        // Caminho do diretório e nome do arquivo
        String diretorio = DEFAULT_PATH_OUTPUT_EXCEL;
        String nomeArquivo = "example.xlsx";

        // Chamada do método para criar e gravar o arquivo Excel
        try {
            gerarArquivoExcel(listaObjetos, diretorio, nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarArquivoExcel(ArrayList<Objeto> listaObjetos, String diretorio, String nomeArquivo) throws IOException {
        // Criando um diretório se não existir
        File directory = new File(diretorio);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Criando um novo livro de trabalho (workbook)
        Workbook workbook = new XSSFWorkbook();

        // Criando uma nova planilha (sheet)
        Sheet sheet = workbook.createSheet("Planilha1");

        int rowIndex = 0;

        for (Objeto obj : listaObjetos) {
            int rowStart = rowIndex;
            int rowEnd = rowStart + Math.max(1, obj.getListaStrings().size()) - 1;

            // Mesclar células para o nome do objeto
            sheet.addMergedRegion(new CellRangeAddress(rowStart, rowEnd, 0, 0));

            // Criar linha para o nome do objeto
            Row row = sheet.createRow(rowIndex++);
            Cell cell = row.createCell(0);
            cell.setCellValue(obj.getNome());

            // Preencher células com strings associadas ao objeto
            if (!obj.getListaStrings().isEmpty()) {
                int colIndex = 1;
                for (String str : obj.getListaStrings()) {
                    Row dataRow = (rowIndex <= rowEnd) ? sheet.getRow(rowIndex++) : sheet.createRow(rowIndex++);
                    Cell dataCell = dataRow.createCell(colIndex);
                    dataCell.setCellValue(str);
                }
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

class Objeto {
    private String nome;
    private ArrayList<String> listaStrings;

    public Objeto(String nome, ArrayList<String> listaStrings) {
        this.nome = nome;
        this.listaStrings = listaStrings;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<String> getListaStrings() {
        return listaStrings;
    }
}
