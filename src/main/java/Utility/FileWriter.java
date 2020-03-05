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

    public static void storeScoreInFileAsCsv(AbstractMap<Integer, AbstractMap<String, Double>> scoresForAllIterations, String fileName){
        List<String> csvList = turnMapIntoListOfCsvStrings(scoresForAllIterations);
        appendToFile(csvList, fileName);
    }

    private static List<String> turnMapIntoListOfCsvStrings(AbstractMap<Integer, AbstractMap<String, Double>> mapOfMap) {
        // list to return
        List<String> csvList = new ArrayList<>();

        // temporary map
        AbstractMap<String, Double> mapOfDoubles;

        // pick out each interations map
        for (Integer firstKey : mapOfMap.keySet()) {

            mapOfDoubles = mapOfMap.get(firstKey);

            // pick out each key and score in the iteration
            for (String secondKey : mapOfDoubles.keySet()) {

                // save as csv
                csvList.add(secondKey + "," + mapOfDoubles.get(secondKey));
            }
        }

        return csvList;
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
