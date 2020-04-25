package Simulator;

import Retriever.NewRetriever;
import Retriever.RetrieverWrapper;
import Settings.Settings;
import Utility.General;
import Utility.FileWriter;

import javax.json.*;
import java.util.*;

public class Simulator {

    public static void runAllIterations(Settings settings, List<String> listOfQueryTerms) throws Exception {

        List<JsonObject> simsAsListOfJsons = new ArrayList<>();

        for (int itr= 0; itr < settings.getNumOfItr(); itr++) {
            /* --- Querying Elastic --- */
            // pick out this iterations query,
            List<String> query = listOfQueryTerms.subList(itr*settings.getSizeOfQuery(), (itr+1)*settings.getSizeOfQuery());
            // re/**/trieve the search result lists corresponding this iterations query
            JsonObject singleSearchResult = RetrieverWrapper.retrieveSearchResults(query, settings.getSizeOfRetrievedList());

/*
            System.out.println("running iteration " + itr);
*/

            // Prepare for applying the expansion component
            AbstractMap<String, Double> scoredDocs = createScoredDocs(singleSearchResult);
            AbstractMap<String, Set<String>> v1 = createV1(singleSearchResult);
            AbstractMap<String, Set<String>> v2 = createV2(v1);

            // For each different combination of parameter values, apply component, apply metrics, and store results
            for (AbstractMap<String, Double> fcnParams : settings.getParamCombs() ) {

                // Rescore wtr to expansion
                AbstractMap<String, Double> expandedResults =
                        FeatureExpander.expandRanking(scoredDocs,v1,v2,fcnParams);

                // pick out the first x of the scored docs
                // TODO: 2020-04-24 just rank the list, dont trim
                List<General.Pair> rankedResults = General.listRankedResults(expandedResults, settings.getSizeOfFinalRankedList());

/*
                System.out.println("In fcnparam loop " + settings.getParamCombs().size());
*/

/*                for (General.Pair p : rankedResults) {
                    System.out.println("key " + p.getKey() + " value " + p.getValue());
                }*/

                //MeasuresWrapper.run2(settings);
                //MeasuresWrapper.compareWithRBD(settings);
                //RankBiasedClusters.run();




                // TODO: 2020-04-24 save metrics in json
                // save ranked list as json in array
                //simsAsListOfJsons.add(JsonCreator.rankedListToJson(rankedListAsList, settings, expMultiplier, 1, itr));
            }
        }

        FileWriter.storeResultsInFileAsJson(simsAsListOfJsons, settings.getSimulationPath());
    }

    public static AbstractMap<String, Double> createScoredDocs(JsonObject singleSearchResult){
/*
        System.out.println("Unchecked scored docs");
*/

        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            scoredDocs.put(id, document.getJsonNumber("score").doubleValue());
        }

        return scoredDocs;
    }

    public static AbstractMap<String, Set<String>> createV1(JsonObject singleSearchResult){
/*
        System.out.println("Unchecked v1");
*/

        AbstractMap<String, Set<String>> v1 = new HashMap<>();
        Set<String> allLinkedDocs = new HashSet<>();
        Set<String> v1Set;

        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            v1Set = new HashSet<>();
            v1Set.add(id);

            JsonArray linkedDocs = document.getJsonArray("citations");
            for (JsonValue jsVal : linkedDocs) {
                v1Set.add(jsVal.toString());
                allLinkedDocs.add(jsVal.toString());
            }
            v1.put(id, v1Set);
        }

        for (String linkedId: allLinkedDocs) {
            v1Set = new HashSet<>();
            v1Set.add(linkedId);
            if (!v1.containsKey(linkedId)){
                v1.put(linkedId, v1Set);
            }
        }

        return v1;
    }

    public static AbstractMap<String, Set<String>> createV2(AbstractMap<String, Set<String>> v1){
/*
        System.out.println("Unchecked v2");
*/
        System.out.println("Include new docs in v2");
/*        // list for the ids in the result for this setting
        List<String> settingsList = new ArrayList<>();

        // loop through all pairs in single result
        for (General.Pair pair: allResults.get(simNr).get(key)) {
            settingsList.add(pair.getKey());
        }
        // retrieve all linked documents
        List<List<String>> retrievedForSettings = NewRetriever.multiGetList(settingsList);*/

        AbstractMap<String, Set<String>> v2 = new HashMap<>();

        for (String id: v1.keySet()) {
            Set mySet = new HashSet();

            for (String innerId: v1.get(id)) {
                mySet.addAll(v1.get(innerId));
            }

            v2.put(id,mySet);
        }
        return v2;
    }
}
