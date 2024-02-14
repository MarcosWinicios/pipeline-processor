package com.marcos.processor.test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

public class TestCSV {

	public static void main(String[] args) {
		String csvPathDirectory = "/home/marcos-winicios/Workspaces/tmp/csvOutputFiles/";
		String csvFileName = "novoCsv.csv";

		String csvPath = csvPathDirectory + csvFileName;

		try {
			
			File directory = new File(csvPath).getParentFile();
			if (!directory.exists()) {
				directory.mkdirs();
			}

			// Criar o arquivo se não existir
			File file = new File(csvPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(csvPath);
			CSVWriter cw = new CSVWriter(fw);

			String[] headers = { "method", "api" };

			List<String[]> data = new ArrayList<String[]>();

			String[] item1 = { "GET", "/account/balance" };
			String[] item2 = { "POST", "/account/balance" };
			String[] item3 = { "PUT", "/account/balance" };
			String[] item4 = { "DELETE", "/account/balance" };

			data.add(headers);

			data.add(item1);
			data.add(item2);
			data.add(item3);
			data.add(item4);
			
			cw.writeAll(data);
			
			cw.close();
			fw.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}
}
