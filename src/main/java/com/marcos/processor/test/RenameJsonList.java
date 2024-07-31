package com.marcos.processor.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RenameJsonList{

    public static void main(String[] args) {
        // Diretório de entrada e saída

        String inputDirectory = "inputFiles/json"; // Nome do diretório com os arquivos JSON
        String outputDirectory = "outputFiles/json"; // Nome do diretório onde os novos arquivos serão criados

        // Criação do diretório de saída, se não existir
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Processamento de todos os arquivos JSON no diretório de entrada
        File inputDir = new File(inputDirectory);
        File[] files = inputDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            System.out.println("Nenhum arquivo JSON encontrado no diretório de entrada.");
            return;
        }

        for (File file : files) {
            try {
                processJsonFile(file, outputDirectory);
            } catch (IOException e) {
                System.out.println("Erro ao processar o arquivo " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Processa um arquivo JSON, extrai o valor do atributo 'fileName' e cria um novo arquivo com esse nome.
     *
     * @param file            O arquivo JSON a ser processado.
     * @param outputDirectory O diretório onde o novo arquivo JSON será salvo.
     * @throws IOException    Se ocorrer um erro de leitura ou escrita.
     */
    public static void processJsonFile(File file, String outputDirectory) throws IOException {
        // Instância de Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Leitura do arquivo JSON
        FileReader reader = new FileReader(file);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        // Obtém o valor do atributo "fileName"
        String fileName = jsonObject.get("name").getAsString();

        // Verifica se o valor do atributo não está vazio
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("O atributo 'fileName' não foi encontrado ou está vazio no arquivo: " + file.getName());
            return;
        }

        // Caminho completo do novo arquivo JSON no diretório de saída
        String outputPath = outputDirectory + File.separator + fileName + ".json";

        // Criação do novo arquivo JSON com o mesmo conteúdo
        FileWriter writer = new FileWriter(outputPath);
        gson.toJson(jsonObject, writer);
        writer.close();

        System.out.println("Arquivo JSON criado: " + outputPath);
    }
}
