package Utility;

import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    /*
    Read specified file and turn each line into json (assume that it is a json object and not a json array)
    Assumes only one JSON per line.
    (Currently not a very well written class, but works where it is used at the moment.)
     */
    public static List<JsonObject> readJsonFromFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(path));

        // list to save all read jsons in
        List<JsonObject> listOfJsons = new ArrayList<>();

        // declare variables used in loop
        StringBuilder sb;
        JsonObject myJson;
        JsonReader jsonRdr;
        String lineAsString;

        // read the first line
        String line = br.readLine();
        while (line != null) {
            // convert line to string
            sb = new StringBuilder(line);
            lineAsString = sb.toString();

            // read the string as a JSON
            jsonRdr = Json.createReader(new StringReader(lineAsString));
            myJson = jsonRdr.readObject(); // assume that it was a json object (and not a json array)
            jsonRdr.close();

            // add to array of all jsons
            listOfJsons.add(myJson);

            // read next line
            line = br.readLine();
        }

        // close buffered reader
        br.close();

        return listOfJsons;
    }
}
