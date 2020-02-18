import javax.json.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class UtilityJsonCreator {
/*    static JsonObject createJsonObjectFromSettings(Settings settings){
        // create date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);

        // create json
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sizeOfFullQuery", settings.getSizeOfFullQuery())
                .add("numOfItr", settings.getNumOfItr())
                .add("sizeOfFinalRankedList", settings.getSizeOfFinalRankedList())
                .add("sizeOfRetrievedList", settings.getSizeOfRetrievedList())
                .add("expansionMultiplier", settings.getExpansionMultiplier())
                .add("numOfSubQueries", settings.getNumOfSubQueries());
        return jsonBuilder.build();
    }*/

    static JsonObject createJsonObjectFromSettings(Settings settings, double expMultiplier, int nrOfSubqueries, int itr){
        // create date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);

        // create json
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sQ", settings.getSizeOfFullQuery()) // sizeOfFullQuery
                .add("sFL", settings.getSizeOfFinalRankedList()) // sizeOfFinalRankedList
                .add("sRL", settings.getSizeOfRetrievedList()) // sizeOfRetrievedList
                .add("itr", itr)
                .add("eM", expMultiplier) // expansionMultiplier
                .add("nSq", nrOfSubqueries); // numOfSubQueries
        return jsonBuilder.build();
    }

    static JsonArray createJsonArrayFromListOfStrings(List<String> list){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String str : list) {
            arrayBuilder.add(str);
        }
        return arrayBuilder.build();
    }

    static JsonArray createJsonArrayFromListOfJsonObjects(List<JsonObject> list){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (JsonObject jsonObj : list) {
            arrayBuilder.add(jsonObj);
        }
        return arrayBuilder.build();
    }

    static JsonObject createJsonFromMapOfJsons(Map<String, JsonValue> jsonMap){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (String key : jsonMap.keySet()) {
            jsonBuilder.add(key, jsonMap.get(key));
        }
        return jsonBuilder.build();
    }

    static List<JsonObject> createListOfJsonObjectsFromListOfListOfPairs(List<List<UtilityGeneral.Pair>> listOfListOfPairs){
        List<JsonObject> listOfJsons  = new ArrayList<>();
        for (List<UtilityGeneral.Pair> listOfPairs : listOfListOfPairs) {
            JsonObject jsonOfPairs = UtilityJsonCreator.createJsonObjectFromListOfPairs(listOfPairs);
            listOfJsons.add(jsonOfPairs);
        }
        return listOfJsons;
    }

    static JsonObject createJsonObjectFromListOfPairs(List<UtilityGeneral.Pair> listedPairs){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (UtilityGeneral.Pair p : listedPairs) {
            jsonBuilder.add(p.getKey(), p.getValue());
        }
        return jsonBuilder.build();
    }

    static JsonArray createJsonObjectFromListDoubles(List<Double> listedDoubles) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Double value : listedDoubles) {
            arrayBuilder.add(value);
        }
        return arrayBuilder.build();
    }
}
