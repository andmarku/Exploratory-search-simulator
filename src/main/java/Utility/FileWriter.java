package Utility;

import javax.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class FileWriter {
    /*
    Wrapper for appendJsonToFile
     */
    public static void storeResultsInFileAsJson(List<JsonObject> jsonList, String fileName){
        List<String> jsonAsStringsList = jsonToListOfStrings(jsonList);
        appendToFile(jsonAsStringsList, fileName);
    }

    public static void storeResultsInFileAsJson(JsonObject json, String fileName){
        List<String> jsonAsStringsList = new ArrayList<>();
        jsonAsStringsList.add(json.toString());
        appendToFile(jsonAsStringsList, fileName);
    }

    private static List<String> jsonToListOfStrings(List<JsonObject> jsonList){
        // list to return
        List<String> jsonAsStringList = new ArrayList<>();

        // add each json as string
        for (JsonObject jsonObject : jsonList) {
            jsonAsStringList.add(jsonObject.toString());
        }

        return jsonAsStringList;
    }

    public static void writeCsv(String itrId, AbstractMap<String, Double> mapOfScores, String fileName){
        List<String> rowsToWrite = new ArrayList<>();
        for (String key : mapOfScores.keySet()) {
            rowsToWrite.add(itrId + "," + key + "," + mapOfScores.get(key));
        }
        appendToFile(rowsToWrite, fileName);
    }

    private static void appendToFile(List<String> listToAppend, String fileName){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            for (String s : listToAppend) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}// end of class
