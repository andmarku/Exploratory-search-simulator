import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonCreator {
    public static JsonObject createPairJsonObject(List<UtilityFunctions.Pair> listedPairs){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (UtilityFunctions.Pair p : listedPairs) {
            jsonBuilder.add(p.getKey(), p.getValue());
        }
        return jsonBuilder.build();
    }

    public static JsonObject createJsonObjectFromSettings(SimulatorSettings settings){
        // create date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);

        // create json
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sizeOfFullQuery", settings.sizeOfFullQuery)
                .add("numOfItr", settings.numOfItr)
                .add("sizeOfFinalRankedList", settings.sizeOfFinalRankedList)
                .add("sizeOfRetrievedList", settings.sizeOfRetrievedList)
                .add("baseCaseExpansionMultiplier", settings.baseCaseExpansionMultiplier)
                .add("baseCaseNumOfSubQueries", settings.baseCaseNumOfSubQueries)
                .add("expansionMultiplier", settings.expansionMultiplier)
                .add("numOfSubQueries", settings.numOfSubQueries);
        return jsonBuilder.build();
    }

    public static JsonObject createJsonObjectFromTwoResults
            (List<UtilityFunctions.Pair> baseList, List<UtilityFunctions.Pair> trialList){
        JsonObject firstJ = createPairJsonObject(baseList);
        JsonObject secondJ = createPairJsonObject(trialList);

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("base_case", firstJ)
                .add("trial_case", secondJ);
        return jsonBuilder.build();
    }
}
