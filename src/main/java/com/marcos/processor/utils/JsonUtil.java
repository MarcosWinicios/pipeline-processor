package com.marcos.processor.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Data;

@Component
@Data
public class JsonUtil {
	
	private static String lastPathDirectoryName;
	private static String pipelinesDirectoryName;

	private static void setAtributesValues(String path) {
		Path pathObject = FileSystems.getDefault().getPath(path);

		String fileName = pathObject.getFileName().toString();
		String parentName = pathObject.getParent().getFileName().toString();

		setLastPathDirectoryName(fileName);
		setPipelinesDirectoryName(parentName);
	}

	public static String toJson(Object obj) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String response = gson.toJson(obj);

		return response;
	}

	public static JsonObject fileToJson(File file) {
		String path = file.getPath();
		return fileToJson(path);
	}

	public static String jsonToFile(Object obj, String path, String fileName) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String fileFullName = path + fileName;

		try {
			// Verificar se o diretório pai existe e criar se necessário
			File directory = new File(fileFullName).getParentFile();
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// Criar o arquivo se não existir
			File file = new File(fileFullName);
			if (!file.exists()) {
				file.createNewFile();
			}

			try (FileWriter writer = new FileWriter(file)) {
				// Converter o objeto Java em JSON e escrever no arquivo
				gson.toJson(obj, writer);
			}
			
			System.err.println("\nArquivo JSON gerado com sucesso em: " + file.getAbsolutePath() + "\n");
			
			return file.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public static JsonObject fileToJson(String pathFile) {

		try {
			// Passo 1: Ler o conteúdo do arquivo JSON como uma string
			String jsonContent = new String(Files.readAllBytes(Paths.get(pathFile)));

			// Passo 2: Analisar a string JSON em um objeto JsonObject
			JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

			return jsonObject;

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private static List<File> getJsonFileList(String path) {

		setAtributesValues(path);

		List<File> responseJsonFiles = new ArrayList<>();

		File[] files = new File(path).listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".json")) {
					responseJsonFiles.add(file);
				}
			}
		}

		return responseJsonFiles;
	}

	public static List<JsonObject> getJsonPipelinesList(String path) {
		List<JsonObject> pipelineList = new ArrayList<>();

		for (File file : getJsonFileList(path)) {
			try {
				FileReader reader = new FileReader(file);
				JsonObject pipeline = JsonParser.parseReader(reader).getAsJsonObject();
				pipelineList.add(pipeline);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}

		return pipelineList;
	}

	public static String getLastPathDirectoryName() {
		return lastPathDirectoryName;
	}

	public static void setLastPathDirectoryName(String lastPathDirectoryName) {
		JsonUtil.lastPathDirectoryName = lastPathDirectoryName;
	}

	public static String getPipelinesDirectoryName() {
		return pipelinesDirectoryName;
	}

	public static void setPipelinesDirectoryName(String pipelinesDirectoryName) {
		JsonUtil.pipelinesDirectoryName = pipelinesDirectoryName;
	}

}
