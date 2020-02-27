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

            for (int nrOfSubqueries : settings.getSubqueries()) {
                for (double expMultiplier: settings.getExpMultipliers()) {

                    // create result
                    List<General.Pair> rankedListAsList = SearchEngine.produceRankedList(settings, mQuery, expMultiplier, nrOfSubqueries);

                    // save ranked list as json in array
                    simsAsListOfJsons.add(JsonCreator.rankedListToJson(rankedListAsList, settings, expMultiplier, nrOfSubqueries, itr));
                }
            }
        }

        StoreInFile.storeResultsInFile(simsAsListOfJsons, settings.getPathToFolder(), settings.getSimulationName());
    }


}
