package Utility;

import javax.json.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileWriter {
    /*
    Wrapper for appendJsonToFile
     */
    public static void storeResultsInFile(List<JsonObject> jsonList, String fileName){
        appendJsonToFile(jsonList, fileName);
    }

    /*
    Simply adds the jsons to the end of the specified file
     */
    private static void appendJsonToFile(List<JsonObject> jsonList, String fileName){
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
