package com.marcos.processor.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RenameJson {

    final static String inputPath = "inputFiles/";
    final static String outputPath = "outputFiles/";
    public static void main(String[] args) {


        // Name of the input JSON file
        String inputFileName = "TCRK-BILL-PAYMENT.json";
        String inputFile = inputPath + inputFileName;
        // Gson instance
        Gson gson = new Gson();

        try {
            // Reading the original JSON file
            FileReader reader = new FileReader(inputFile);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Obtaining the value of the attribute "fileName"
            String fileName = jsonObject.get("name").getAsString();

            // Checking if the value is not empty
            if (fileName == null || fileName.isEmpty()) {
                System.out.println("The attribute 'fileName' was not found or is empty in the JSON.");
                return;
            }

            // Name of the new JSON file
            String outputFile = outputPath + fileName + ".json";

            // Writing the same content to the new JSON file
            FileWriter writer = new FileWriter(outputFile);
            gson.toJson(jsonObject, writer);
            writer.close();

            System.out.println("JSON file created with the name: " + outputFile);

        } catch (IOException e) {
            System.out.println("Error reading or writing the JSON file: " + e.getMessage());
        }
    }
}
