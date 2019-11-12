import javax.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

class UtilityJsonPrinter {
    static void storeResultsInFile(List<JsonObject> results, String pathToFolder, String simulationName){
        String path = UtilityJsonPrinter.createCompleteFileName(pathToFolder, simulationName);
        UtilityJsonPrinter.myFileWriter( results,path);
    }

    static void printJson(JsonObject json, String fileName) throws FileNotFoundException {
        JsonWriter jsonWriter = Json.createWriter(new FileOutputStream(fileName));
        jsonWriter.writeObject(json);
        jsonWriter.close();
    }

    private static String createCompleteFileName(String pathToFolder, String fileName){
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
