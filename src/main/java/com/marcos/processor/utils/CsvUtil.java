package com.marcos.processor.utils;

import com.marcos.processor.model.Connector;
import com.marcos.processor.model.ConnectorList;
import com.marcos.processor.model.RequestAPI;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUtil {

	/**
	 *
	 * @param connectors                Objeto {@link ConnectorList} que representa
	 *                                  uma lista de connectores
	 * @param directoryTargetPath       Diretório alvo onde o arquivo será salvo
	 * @param fileName                  Nome do arquivo alvo a ser gerado
	 * @param generateWithConnectorName Define se adiciona ou não o nome da Pipeline
	 *                                  em cada connector
	 */
	public static String connectorListToCsv(ConnectorList connectors, String directoryTargetPath, String fileName,
			boolean generateWithConnectorName) {

		List<String[]> data = new ArrayList<>();

		if (generateWithConnectorName) {
			data = generateConnectorCsvDataWithName(connectors);
		} else {
			data = generateConnectorCsvDataWithoutName(connectors);
		}

		return generateCsvFile(data, directoryTargetPath, fileName);

	}

	private static List<String[]> generateConnectorCsvDataWithoutName(ConnectorList connectorList) {
		List<String[]> data = new ArrayList<String[]>();

		String[] headers = { "method".toUpperCase(), "endpoint".toUpperCase() };
		data.add(headers);

		List<RequestAPI> requestApiList = connectorList.extractNoDuplicateRequestAPIList();

		var lines =  requestApiList.stream()
				.map(x -> new String[]{x.getHttpMethod(), x.getEndpoint()})
				.toList();

		data.addAll(lines);

		return  data;
//
//		for (Connector connector : connectorList.getConnectors()) {
//
//			if (connector.getRequestList().isEmpty()) {
//				continue;
//			}
//			RequestAPI api = connector.getRequestList().get(0);
//			String endPointMethod = api.getHttpMethod();
//			String endpointValue = api.getEndpoint();
//
//			String[] item = { endPointMethod, endpointValue };
//			data.add(item);
//
//		}
//
//		return data;
	}

	private static List<String[]> generateConnectorCsvDataWithName(ConnectorList connectorList) {
		List<String[]> data = new ArrayList<String[]>();

		String[] headers = { "connector".toUpperCase(), "method".toUpperCase(), "endpoint".toUpperCase() };
		data.add(headers);

		for (Connector connector : connectorList.getConnectors()) {

			String connectorName = connector.getName();
			String endPointMethod = "";
			String endpointValue = "";

			if (connector.getRequestList().size() == 0) {

				String[] item = { connectorName, endPointMethod, endpointValue };
				data.add(item);
				continue;
			}

			for (RequestAPI api : connector.getRequestList()) {
				endPointMethod = api.getHttpMethod();
				endpointValue = api.getEndpoint();

				String[] item = { connectorName, endPointMethod, endpointValue };
				data.add(item);
			}
		}

		return data;
	}

	/**
	 *
	 * @param data          Informações que irão compor o conteúdo do arquivo CSV
	 * @param directoryPath Diretório alvo onde o arquivo será salvo
	 * @param fileName      Nome do arquivo a ser gerado
	 */
	private static String generateCsvFile(List<String[]> data, String directoryPath, String fileName) {

		try {
			String fileFullName = directoryPath + fileName + ".csv";

			File directory = new File(fileFullName).getParentFile();
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// Criar o arquivo se não existir
			File file = new File(fileFullName);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(fileFullName);
			CSVWriter cw = new CSVWriter(fw);

			cw.writeAll(data);

			System.err.println("\nArquivo CSV gerado com sucesso em: " + file.getAbsolutePath() + "\n");

			cw.close();
			fw.close();
			return file.getAbsolutePath();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
