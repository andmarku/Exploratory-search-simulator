import javax.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UtilityStoreInFile {
/*    static void storeResults(SimulatorSettings settings, String nameOfJsonToStore, JsonValue json, String filename) throws FileNotFoundException {
        Map<String, JsonValue> storageMap = new HashMap<>();

        // store settings
        storageMap.put("settings", UtilityJsonCreator.createJsonObjectFromSettings(settings));

        // store trial cases
        storageMap.put(nameOfJsonToStore, json);

        // save all stored jsons to file
        JsonObject jsonToPrint = UtilityJsonCreator.createJsonFromMapOfJsons(storageMap);
        UtilityStoreInFile.writeJsonToFile(jsonToPrint, settings.pathToFolder, filename);
    }*/

    static void storeResults(SimulatorSettings settings, String filename, List<String> nameOfJsonToStore, List<JsonValue> jsonsToStore) throws Exception {
        if(nameOfJsonToStore.size() != jsonsToStore.size()){
            throw new Exception("List of different sizes");
        }

        Map<String, JsonValue> storageMap = new HashMap<>();

        // store settings
        storageMap.put("settings", UtilityJsonCreator.createJsonObjectFromSettings(settings));

        // store jsons
        for (int i = 0; i < jsonsToStore.size(); i++) {
            storageMap.put(nameOfJsonToStore.get(i), jsonsToStore.get(i));
        }
        // save all stored jsons to file
        JsonObject jsonToPrint = UtilityJsonCreator.createJsonFromMapOfJsons(storageMap);
        UtilityStoreInFile.writeJsonToFile(jsonToPrint, settings.pathToFolder, filename);
    }

    static void storeResultsInFile(List<JsonObject> results, String pathToFolder, String simulationName){
        String path = UtilityStoreInFile.createCompleteFileName(pathToFolder, simulationName);
        UtilityStoreInFile.myFileWriter( results,path);
    }


    static void writeJsonToFile(JsonObject json, String pathToFolder, String simulationName) throws FileNotFoundException {
        String path = UtilityStoreInFile.createCompleteFileName(pathToFolder, simulationName);
        JsonWriter jsonWriter = Json.createWriter(new FileOutputStream(path));
        jsonWriter.writeObject(json);
        jsonWriter.close();
    }

    static String createCompleteFileName(String pathToFolder, String fileName){
        return pathToFolder + fileName;
    }

    private static void myFileWriter(List<JsonObject> jsonList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
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
