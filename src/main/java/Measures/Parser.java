package Measures;

import Utility.General;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static /*List<List<General.Pair>>*/ void parseListOfSimulationResults(List<JsonObject> simsFromFile){
        List<List<General.Pair>> listsSingleMasterQuery = new ArrayList<>();

        System.out.println(simsFromFile);
        /*JsonArray myJsonArray = simsFromFile.getJsonArray("baseLines");

        List<General.Pair> resultList;
        for (int i = 0; i < myJsonArray.size(); i++) {
            resultList = new ArrayList<>();
            JsonObject listAsJson = myJsonArray.getJsonObject(i);
            for (String key : listAsJson.keySet()) {
                General.Pair pair = new General.Pair(key, listAsJson.getJsonNumber(key).doubleValue());
                resultList.add(pair);
            }
            listsSingleMasterQuery.add(resultList);
        }
        return listsSingleMasterQuery;*/
    }
}
