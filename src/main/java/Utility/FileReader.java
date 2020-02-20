package Utility;

import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public static List<String> readMasterQueries(JsonObject jsonFromFile){
        JsonArray queriesAsJsonArray = jsonFromFile.getJsonArray("queryTerms");
        List<String> queriesAsList = new ArrayList<>();
        for (int i = 0; i < queriesAsJsonArray.size(); i++) {
            queriesAsList.add(queriesAsJsonArray.getString(i));
        }
        return queriesAsList;
    }

    public static List<List<General.Pair>> parseListOfSearchResults(JsonObject baseLinesFromFile){
        List<List<General.Pair>> allLists = new ArrayList<>();

        JsonArray myJsonArray = baseLinesFromFile.getJsonArray("baseLines");

        List<General.Pair> resultList;
        for (int i = 0; i < myJsonArray.size(); i++) {
            resultList = new ArrayList<>();
            JsonObject listAsJson = myJsonArray.getJsonObject(i);
            for (String key : listAsJson.keySet()) {
                General.Pair pair = new General.Pair(key, listAsJson.getJsonNumber(key).doubleValue());
                resultList.add(pair);
            }
            allLists.add(resultList);
        }
        return allLists;
    }

    public static JsonObject readJsonFromFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(path));
        JsonObject myJson;
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        String jsonString = sb.toString();

        br.close();
        // read the string as a JSON
        JsonReader jsonRdr = Json.createReader(new StringReader(jsonString));
        myJson = jsonRdr.readObject(); // assume that it was a json object (and not a json array)
        jsonRdr.close();

        return myJson;

    }
}
