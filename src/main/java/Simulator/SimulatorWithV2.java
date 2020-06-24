package Simulator;

import Measures.MeasuresWrapper;
import Retriever.NewRetriever;
import Retriever.RetrieverParser;
import Retriever.RetrieverWrapper;
import Settings.Settings;
import Utility.FileWriter;
import Utility.General;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.*;

public class SimulatorWithV2 {
    public static void runAllIterations(Settings settings, List<String> listOfQueryTerms) throws Exception {

        for (int itr= 0; itr < settings.getNumOfItr(); itr++) {

            /* --- Querying Elastic --- */
            // pick out this iterations query,
            List<String> query = listOfQueryTerms.subList(itr*settings.getSizeOfQuery(), (itr+1)*settings.getSizeOfQuery());
            // re/**/trieve the search result lists corresponding this iterations query
            JsonObject singleSearchResult = RetrieverWrapper.retrieveSearchResults(query, settings.getSizeOfRetrievedList());

            String itrId = "itr=" + itr;

            // Prepare for applying the expansion component
            AbstractMap<String, Double> scoredDocs = createScoredDocs(singleSearchResult);
            AbstractMap<String, Double> topScoredDocs = pickOutTheTopMap(scoredDocs, settings.getMaxSizeToExpandToV1());
            AbstractMap<String, Set<String>> v1 = createV1(singleSearchResult, scoredDocs);
            AbstractMap<String, Set<String>> topV1 = createV1(singleSearchResult, topScoredDocs);
            AbstractMap<String, Set<String>> v2 = createV2(v1);

            System.out.println("Size of scored docs " + scoredDocs.size());
            System.out.println("Size of v1 " + v1.size());
            System.out.println("Size of v2 " + v2.size());

            // For each different combination of parameter values, apply component, apply metrics, and store results
            Map<String, List<General.Pair>> mapOfAllListsFromIteration = new HashMap<>();

            // loop through list of list
            for (List<Double> fcnParams : settings.getParamCombs() ) {

                // Rescore wtr to expansion
                AbstractMap<String, Double> expandedResults = FeatureExpanderWithV2.expandRanking(scoredDocs,v1,v2,fcnParams);

                // order the scored docs
                List<General.Pair> rankedResults = General.listRankedResults(expandedResults, settings.getMaxSizeOfFinalList());

                // create key for storing results
                String paramName = parametersToString(fcnParams);

                // store results from this param combo
                mapOfAllListsFromIteration.put(paramName, rankedResults);
            }

            // score the results from this query
            AbstractMap<String, AbstractMap<String, Double>> mapOfMeasures = MeasuresWrapper.measureAll(settings,
                    mapOfAllListsFromIteration, v2);

            AbstractMap<String, Double> rbOverlap = mapOfMeasures.get("rbo");
            FileWriter.writeCsv(itrId, Integer.toString(settings.getSizeOfQuery()),rbOverlap, settings.getRbOverlapPath());

            AbstractMap<String, Double> rbCluster = mapOfMeasures.get("rbc");
            FileWriter.writeCsv(itrId, Integer.toString(settings.getSizeOfQuery()), rbCluster, settings.getRbClusterPath());

            AbstractMap<String, Double> rbSampling = mapOfMeasures.get("rbs");
            FileWriter.writeCsv(itrId, Integer.toString(settings.getSizeOfQuery()), rbSampling, settings.getRbSamplingPath());

        }
    }

    public static AbstractMap<String, Set<String>> createV2(AbstractMap<String, Set<String>> v1){
        AbstractMap<String, Set<String>> v2 = new HashMap<>();

        // v2 is a larger version of v1
        v2.putAll(v1);

        // loop through the keys in v1 in order to add the rest of the docs from the mapOfAll
        for (String docInV1 : v1.keySet()) {

            // include all the new docs (keys) in v2 that are not in v1
            for (String linkedDoc : v1.get(docInV1)) {

                // if the doc is not already in v2, then add it with the v1Set as its v2set
                if (!v2.containsKey(linkedDoc)){
                    Set<String> newV2SetToAdd = new HashSet<>();
                    // newV2SetToAdd.addAll(v1.get(docInV1));
                    v2.put(linkedDoc, newV2SetToAdd);
                }
            /*// if the doc is in v2, complete its set with the the v1Set
            else{
                // get the set from the v2 map, in order to add to it (must exist thanks to containsKey statement above)
                Set<String> v2SetToUpdate = v2.get(linkedDoc);
                v2SetToUpdate.addAll(v1.get(docInV1));
                // probably not needed, but I'll include it anyway
                v2.put(linkedDoc, v2SetToUpdate);
            }*/
            }
        }
        return v2;
    }

    public static AbstractMap<String, Set<String>> createV1(JsonObject singleSearchResult, AbstractMap<String, Double> topScoredDocs) throws Exception {
        AbstractMap<String, Set<String>> v1 = new HashMap<>();
        Set<String> allLinkedDocs = new HashSet<>();

        for (String id : singleSearchResult.keySet()) {
            if (!topScoredDocs.containsKey(id)){
                continue;
            }
            JsonObject document = singleSearchResult.getJsonObject(id);
            Set<String> v1Set = new HashSet<>();
            v1Set.add(id);

            JsonArray linkedDocs = document.getJsonArray("citations");
            for (JsonValue jsVal : linkedDocs) {
                //hack to remove citation marks
                String linkedId = RetrieverParser.removeCitationMarks(jsVal.toString());
                v1Set.add(linkedId);
                allLinkedDocs.add(linkedId);
            }
            v1.put(id, v1Set);
        }

        // Docs to retrieve
        List<String> docsToRetrieve = new ArrayList<>();
        for (String linkedId: allLinkedDocs) {
            // if the linked doc was not in the original hits (or has been linked to before?)
            if (!v1.containsKey(linkedId)){
                docsToRetrieve.add(linkedId);
            }
        }

        // add the linked docs with their sets
        AbstractMap<String, List<String>> retrievedDocs = NewRetriever.multiGetList(docsToRetrieve);
        for (String linkedId: retrievedDocs.keySet()) {
            Set<String> v1Set = new HashSet<>();
            v1Set.addAll(retrievedDocs.get(linkedId));
            v1Set.add(linkedId);
            v1.put(linkedId, v1Set);
        }
        return v1;
    }

    public static AbstractMap<String, Double> createScoredDocs(JsonObject singleSearchResult){
        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            scoredDocs.put(id, document.getJsonNumber("score").doubleValue());
        }
        return scoredDocs;

    }

    public static AbstractMap<String, Double> pickOutTheTopMap(AbstractMap<String, Double> scoredDocs, int maxSize){
        AbstractMap<String, Double> topScoredDocs = new HashMap<>();
        List<General.Pair> rankedResults = General.listRankedResults(scoredDocs, maxSize);
        for (General.Pair p : rankedResults) {
            topScoredDocs.put(p.getKey(), p.getValue());
        }
        return topScoredDocs;
    }

    public static String parametersToString(List<Double> fcnParams){
        StringBuilder settingsAsCsvString = new StringBuilder();
        String stringToAdd = "";
        for (Double paramValue : fcnParams) {
            settingsAsCsvString.append(stringToAdd).append(paramValue);
            stringToAdd = ",";
        }
        return settingsAsCsvString.toString();
    }
}
