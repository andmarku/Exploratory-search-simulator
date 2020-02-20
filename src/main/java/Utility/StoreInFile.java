package Utility;

import Settings.Settings;
import javax.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreInFile {
/*    public static void storeResults(SimulatorSettings settings, String nameOfJsonToStore, JsonValue json, String filename) throws FileNotFoundException {
        Map<String, JsonValue> storageMap = new HashMap<>();

        // store settings
        storageMap.put("settings", Utility.UtilityJsonCreator.createJsonObjectFromSettings(settings));

        // store trial cases
        storageMap.put(nameOfJsonToStore, json);

        // save all stored jsons to file
        JsonObject jsonToPrint = Utility.UtilityJsonCreator.createJsonFromMapOfJsons(storageMap);
        Utility.UtilityStoreInFile.writeJsonToFile(jsonToPrint, settings.pathToFolder, filename);
    }*/

/*    public static void storeResults(Settings.Settings settings, String filename, List<String> nameOfJsonToStore, List<JsonValue> jsonsToStore) throws Exception {
        if(nameOfJsonToStore.size() != jsonsToStore.size()){
            throw new Exception("List of different sizes");
        }

        Map<String, JsonValue> storageMap = new HashMap<>();

        // store settings
        storageMap.put("settings", Utility.UtilityJsonCreator.createJsonObjectFromSettings(settings));

        // store jsons
        for (int i = 0; i < jsonsToStore.size(); i++) {
            storageMap.put(nameOfJsonToStore.get(i), jsonsToStore.get(i));
        }

        // save all stored jsons to file
        JsonObject jsonToPrint = Utility.UtilityJsonCreator.createJsonFromMapOfJsons(storageMap);
        Utility.UtilityStoreInFile.writeJsonToFile(jsonToPrint, settings.getPathToFolder(), filename);
    }*/

    public static void storeQueries(Settings settings, String filename, List<String> nameOfJsonToStore, List<JsonValue> jsonsToStore) throws Exception {
        if(nameOfJsonToStore.size() != jsonsToStore.size()){
            throw new Exception("List of different sizes");
        }

        Map<String, JsonValue> storageMap = new HashMap<>();

        // create json for settings
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sizeOfFullQuery", settings.getSizeOfFullQuery())
                .add("itr", settings.getNumOfItr());

        // store settings
        storageMap.put("settings", jsonBuilder.build());

        // store jsons
        for (int i = 0; i < jsonsToStore.size(); i++) {
            storageMap.put(nameOfJsonToStore.get(i), jsonsToStore.get(i));
        }

        // save all stored jsons to file
        JsonObject jsonToPrint = JsonCreator.createJsonFromMapOfJsons(storageMap);
        StoreInFile.writeJsonToFile(jsonToPrint, settings.getPathToFolder(), filename);
    }

    public static void storeResultsInFile(List<JsonObject> results, String pathToFolder, String simulationName){
        String path = StoreInFile.createCompleteFileName(pathToFolder, simulationName);
        StoreInFile.myFileWriter( results,path);
    }


    public static void writeJsonToFile(JsonObject json, String pathToFolder, String simulationName) throws FileNotFoundException {
        String path = StoreInFile.createCompleteFileName(pathToFolder, simulationName);
        JsonWriter jsonWriter = Json.createWriter(new FileOutputStream(path, true));
        jsonWriter.writeObject(json);
        jsonWriter.close();
    }

    public static String createCompleteFileName(String pathToFolder, String fileName){
        return pathToFolder + fileName;
    }

    private static void myFileWriter(List<JsonObject> jsonList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName, true), StandardCharsets.UTF_8))) {
            writer.write(jsonList.get(0).toString());
            for (int i = 1; i < jsonList.size(); i++) {
                writer.newLine();
                writer.write(jsonList.get(i).toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}// end of class
