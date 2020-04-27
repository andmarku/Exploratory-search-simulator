package Utility;

import Settings.Settings;
import javax.json.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class JsonCreator {
    public static JsonObject createJsonObjectFromSettings(Settings settings, double expMultiplier, int nrOfSubqueries, int itr){
        // create date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);

        // create json
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sQ", settings.getSizeOfQuery()) // sizeOfFullQuery
                .add("sRL", settings.getSizeOfRetrievedList()) // sizeOfRetrievedList
                .add("itr", itr)
                .add("eM", expMultiplier) // expansionMultiplier
                .add("nSq", nrOfSubqueries); // numOfSubQueries
        return jsonBuilder.build();
    }

    public static JsonArray createJsonArrayFromListOfStrings(List<String> list){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String str : list) {
            arrayBuilder.add(str);
        }
        return arrayBuilder.build();
    }

    public static JsonObject createJsonFromMapOfJsons(Map<String, JsonValue> jsonMap){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (String key : jsonMap.keySet()) {
            jsonBuilder.add(key, jsonMap.get(key));
        }
        return jsonBuilder.build();
    }

    public static JsonObject createJsonFromMapOfMapOfDoubles(AbstractMap<String, AbstractMap<String, Double>> myMap, String itrId){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        JsonObjectBuilder innerJsonBuilder;
        AbstractMap<String, Double> innerMap;

        jsonBuilder.add("id", itrId);

        // through outer map
        for (String key : myMap.keySet()) {
            innerJsonBuilder = Json.createObjectBuilder();

            // through inner map
            innerMap = myMap.get(key);
            for (String innerKey : innerMap.keySet()) {
                innerJsonBuilder.add(innerKey, innerMap.get(innerKey));
            }
            jsonBuilder.add(key, innerJsonBuilder.build());
        }

        return jsonBuilder.build();
    }

    public static JsonObject createJsonObjectFromListOfPairs(List<General.Pair> listedPairs){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (General.Pair p : listedPairs) {
            jsonBuilder.add(p.getKey(), p.getValue());
        }
        return jsonBuilder.build();
    }

    public static JsonObject rankedListToJson(List<General.Pair> rankedListAsList, Settings settings, double expMultiplier, int nrOfSubqueries, int itr) {

        // convert ranked list to json object
        JsonObject rankedListAsJson = createJsonObjectFromListOfPairs(rankedListAsList);
        JsonObject settingsAsJson = createJsonObjectFromSettings(settings, expMultiplier, nrOfSubqueries, itr);

        // return final json
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("settings", settingsAsJson);
        jsonBuilder.add("rankedList", rankedListAsJson);
        return jsonBuilder.build();
    }
}
