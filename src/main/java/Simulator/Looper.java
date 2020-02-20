package Simulator;

import SearchEngine.SearchEngine;
import Settings.Settings;
import Utility.FileReader;
import Utility.General;
import Utility.JsonCreator;
import Utility.StoreInFile;

import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class Looper {

    public static void runAllCasesInLoop(Settings settings) throws Exception {
        int sizeOfMQuery = settings.getSizeOfFullQuery();

        // read all queries from file
        JsonObject queriesFromFile = FileReader.readJsonFromFile(settings.getQueryPath());
        List<String> listOfQueryTerms = FileReader.readMasterQueries(queriesFromFile);


        int nrOfLists = settings.getNumOfItr() * settings.getSubqueries().size() * settings.getExpMultipliers().size();
        List<JsonObject> simsAsListOfJsons  = new ArrayList<>(nrOfLists);

        for (int itr= 0; itr < settings.getNumOfItr(); itr++) {

            // pick out this iterations master query,
            List<String> mQuery = listOfQueryTerms.subList(itr*sizeOfMQuery, (itr+1)*sizeOfMQuery);

            for (int nrOfSubqueries : settings.getSubqueries()){
                for (double expMultiplier: settings.getExpMultipliers()) {

                    // create result
                    List<General.Pair> rankedListAsList = SearchEngine.produceRankedList(settings, mQuery, expMultiplier, nrOfSubqueries);

                    // convert to json object
                    JsonObject rankedListAsJson = JsonCreator.createJsonObjectFromListOfPairs(rankedListAsList);
                    JsonObject settingsAsJson = JsonCreator.createJsonObjectFromSettings(settings, expMultiplier, nrOfSubqueries, itr);

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

        StoreInFile.storeResultsInFile(simsAsListOfJsons, settings.getPathToFolder(), settings.getSimulationName());
    }


/*


    static void mySimulator(Settings.Settings settings) throws Exception {
        // run simulation
        List<List<Utility.UtilityGeneral.Pair>> mySims = runSimulation(settings);

*//*
        // score results
        List<Double> scores = compareSimulationWithBaseLine(settings, mySims);
*//*

        // store resulting list
        List<JsonObject> simulationsAsListOfJsons = Utility.UtilityJsonCreator.createListOfJsonObjectsFromListOfListOfPairs(mySims);
        JsonArray trialCases = Utility.UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulationsAsListOfJsons);

        List<String> nameOfJsonsToStore = new ArrayList<>();
        nameOfJsonsToStore.add("trials");
        nameOfJsonsToStore.add("scores");

        List<JsonValue> jsonsToStore = new ArrayList<>();
        jsonsToStore.add(trialCases);
        *//*jsonsToStore.add(Utility.UtilityJsonCreator.createJsonObjectFromListDoubles(scores));*//*

        Utility.UtilityStoreInFile.storeResults(settings, settings.getSimulationName(), nameOfJsonsToStore, jsonsToStore);
    }*/

}
