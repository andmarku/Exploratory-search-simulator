import javax.json.*;
import java.io.*;
import java.util.List;

public class JsonPrinter {
    public static JsonObject createPairJsonObject(List<UtilityFunctions.Pair> listedPairs){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (UtilityFunctions.Pair p : listedPairs) {
            jsonBuilder.add(p.getKey(), p.getValue());
        }
        return jsonBuilder.build();
    }

    // TODO: 2019-11-04 expand upon
    public static JsonObject createJsonObjectFromSettings
            (String date, int sizeOfFullQuery, int numOfItr, int sizeOfFinalRankedList,
             int sizeOfRetrievedList, int numOfSubQueries, int baseCaseNumOfSubQueries,
             double expansionMultiplier, double baseCaseExpansionMultiplier){
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sizeOfFullQuery", sizeOfFullQuery)
                .add("numOfItr", numOfItr)
                .add("sizeOfFinalRankedList", sizeOfFinalRankedList)
                .add("sizeOfRetrievedList", sizeOfRetrievedList)
                .add("baseCaseExpansionMultiplier", baseCaseExpansionMultiplier)
                .add("baseCaseNumOfSubQueries", baseCaseNumOfSubQueries)
                .add("expansionMultiplier", expansionMultiplier)
                .add("numOfSubQueries", numOfSubQueries);
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

    public static String createCompleteFileName(String pathToFolder, String fileName){
        StringBuilder sb = new StringBuilder();
        sb.append(pathToFolder);
        sb.append(fileName);
        return sb.toString();
    }

    public static void printJson(JsonObject json, String fileName) throws FileNotFoundException {
        JsonWriter jsonWriter = Json.createWriter(new FileOutputStream(fileName));
        jsonWriter.writeObject(json);
        jsonWriter.close();
    }

    public static void myFileWriter(List<JsonObject> jsonList, String fileName) throws FileNotFoundException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "utf-8"))) {
            writer.write(jsonList.get(0).toString());
            for (int i = 1; i < jsonList.size(); i++) {
                writer.newLine();
                writer.write(jsonList.get(i).toString());
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}// end of class
