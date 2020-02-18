import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class SimulatorLooper {

    static void runAllCasesInLoop(Settings settings) throws Exception {
        int sizeOfMQuery = settings.getSizeOfFullQuery();

        // read all queries from file
        JsonObject queriesFromFile = UtilityFileReader.readJsonFromFile(settings.getQueryPath());
        List<String> listOfQueryTerms = UtilityFileReader.readMasterQueries(queriesFromFile);


        int nrOfLists = settings.getNumOfItr() * settings.getSubqueries().size() * settings.getExpMultipliers().size();
        List<JsonObject> simsAsListOfJsons  = new ArrayList<>(nrOfLists);

        for (int itr= 0; itr < settings.getNumOfItr(); itr++) {

            // pick out this iterations master query,
            List<String> mQuery = listOfQueryTerms.subList(itr*sizeOfMQuery, (itr+1)*sizeOfMQuery);

            for (int nrOfSubqueries : settings.getSubqueries()){
                for (double expMultiplier: settings.getExpMultipliers()) {

                    // create result
                    List<UtilityGeneral.Pair> rankedListAsList = SimulatorSearch.produceRankedList(settings, mQuery, expMultiplier, nrOfSubqueries);

                    // convert to json object
                    JsonObject rankedListAsJson = UtilityJsonCreator.createJsonObjectFromListOfPairs(rankedListAsList);
                    JsonObject settingsAsJson = UtilityJsonCreator.createJsonObjectFromSettings(settings, expMultiplier, nrOfSubqueries, itr);

                    // create final json
                    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
                    jsonBuilder.add("settings", settingsAsJson);
                    jsonBuilder.add("rankedList", rankedListAsJson);

                    // save to array
                    JsonObject finalJson = jsonBuilder.build();
                    simsAsListOfJsons.add(finalJson);
                }
            }
        }

        UtilityStoreInFile.storeResultsInFile(simsAsListOfJsons, settings.getPathToFolder(), settings.getSimulationName());
    }


/*


    static void mySimulator(Settings settings) throws Exception {
        // run simulation
        List<List<UtilityGeneral.Pair>> mySims = runSimulation(settings);

*//*
        // score results
        List<Double> scores = compareSimulationWithBaseLine(settings, mySims);
*//*

        // store resulting list
        List<JsonObject> simulationsAsListOfJsons = UtilityJsonCreator.createListOfJsonObjectsFromListOfListOfPairs(mySims);
        JsonArray trialCases = UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulationsAsListOfJsons);

        List<String> nameOfJsonsToStore = new ArrayList<>();
        nameOfJsonsToStore.add("trials");
        nameOfJsonsToStore.add("scores");

        List<JsonValue> jsonsToStore = new ArrayList<>();
        jsonsToStore.add(trialCases);
        *//*jsonsToStore.add(UtilityJsonCreator.createJsonObjectFromListDoubles(scores));*//*

        UtilityStoreInFile.storeResults(settings, settings.getSimulationName(), nameOfJsonsToStore, jsonsToStore);
    }*/

}
