package Simulator;

import SearchEngine.SearchEngineWrapper;
import SearchEngine.Model;
import SearchEngine.SimpleModel;
import Settings.Settings;
import Utility.FileReader;
import Utility.General;
import Utility.JsonCreator;
import Utility.FileWriter;

import javax.json.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class Looper {

    public static void runAllCasesInLoop(Settings settings) throws Exception {
        int sizeOfMQuery = settings.getSizeOfFullQuery();

        // Choose model (currently only one)
        Model rankingModel = new SimpleModel();

        // read all queries from file
        List<JsonObject> listQueriesFromFile = FileReader.readJsonFromFile(settings.getQueryPath());
        if (listQueriesFromFile.size() > 1){
            System.out.println("Maybe reading in wrong file for the queries!");
        }
        JsonObject queriesFromFile = listQueriesFromFile.get(0);
        List<String> listOfQueryTerms = QueryCreator.parseMasterQueries(queriesFromFile);


        int nrOfLists = settings.getNumOfItr() * settings.getSubqueries().size() * settings.getExpMultipliers().size();
        List<JsonObject> simsAsListOfJsons  = new ArrayList<>(nrOfLists);

        for (int itr= 0; itr < settings.getNumOfItr(); itr++) {
            // pick out this iterations query,
            List<String> query = listOfQueryTerms.subList(itr*sizeOfMQuery, (itr+1)*sizeOfMQuery);

            // retrieve the search result lists corresponding this iterations subquery
            AbstractMap<String, AbstractMap<String, Double>>  searchResult =
                    SearchEngineWrapper.retrieveSearchResultsLists(query, settings.getSizeOfRetrievedList());

            for (double expMultiplier: settings.getExpMultipliers()) {
                // create result
                List<General.Pair> rankedListAsList = rankingModel.produceExpandedRankedList(settings, expMultiplier, searchResult);

                // save ranked list as json in array
                simsAsListOfJsons.add(JsonCreator.rankedListToJson(rankedListAsList, settings, expMultiplier, 1, itr));
            }
        }

        FileWriter.storeResultsInFileAsJson(simsAsListOfJsons, settings.getSimulationPath());
    }


}
