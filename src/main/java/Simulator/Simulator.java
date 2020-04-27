package Simulator;

import Measures.MeasuresWrapper;
import Retriever.NewRetriever;
import Retriever.RetrieverWrapper;
import Settings.Settings;
import Utility.General;
import Utility.FileWriter;
import Utility.JsonCreator;

import javax.json.*;
import java.io.IOException;
import java.util.*;

public class Simulator {

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
            AbstractMap<String, Set<String>> v1 = createV1(singleSearchResult);
            AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1 = getMapOfEntireV1(v1);
            AbstractMap<String, Set<String>> v2 = createV2(v1, mapOfAllDocsInAllOfV1);

            // For each different combination of parameter values, apply component, apply metrics, and store results
            Map<String, List<General.Pair>> mapOfAllListsFromIteration = new HashMap<>();
            for (AbstractMap<String, Double> fcnParams : settings.getParamCombs() ) {

                // Rescore wtr to expansion
                AbstractMap<String, Double> expandedResults = FeatureExpander.expandRanking(scoredDocs,v1,v2,fcnParams);

                // order the scored docs
                List<General.Pair> rankedResults = General.listRankedResults(expandedResults, settings.getMaxSizeOfFinalList());

                // create key for storing results
                String paramName = parametersToString(fcnParams);

                // store results from this param combo
                mapOfAllListsFromIteration.put(paramName, rankedResults);
            }

            // score the results from this query
            AbstractMap<String, AbstractMap<String, Double>> mapOfMeasures = MeasuresWrapper.measureAll(settings,
                    mapOfAllListsFromIteration, mapOfAllDocsInAllOfV1);

            // create json from measures
            JsonObject jsonToWriteToFile = JsonCreator.createJsonFromMapOfMapOfDoubles(mapOfMeasures, itrId);

            // write json to file
            FileWriter.storeResultsInFileAsJson(jsonToWriteToFile, settings.getScorePath());
        }
    }

    public static AbstractMap<String, Set<String>> getMapOfEntireV1(AbstractMap<String, Set<String>> v1) throws IOException {

        AbstractMap<String, Set<String>> alreadyRetrievedDocs = new HashMap<>();
        alreadyRetrievedDocs.putAll(v1);

        for (String key : v1.keySet()) {
            // pick out only those docs that are not already retrieved
            List<String> docsToRetrieve = getLinkedDocsToRetrieve(alreadyRetrievedDocs, v1.get(key));

            // retrieve from elastic
            AbstractMap<String, List<String>> retrievedDocs = NewRetriever.multiGetList(docsToRetrieve);

            // add the docs from the v1 to the alreadyRetrievedDocs
            for (String innerKey : retrievedDocs.keySet()) {
                // put into the right format (set, not list)
                Set<String> innerSet = new HashSet<>();
                innerSet.addAll(retrievedDocs.get(innerKey));

                // add
                alreadyRetrievedDocs.put(innerKey, innerSet);
            }
        }
        return alreadyRetrievedDocs;
    }

    public static List<String> getLinkedDocsToRetrieve(AbstractMap<String, Set<String>> alreadyRetrievedDocs, Set<String> docsToExamine){
        List<String> docsToRetrieve = new ArrayList<>();
        for (String id : docsToExamine) {
            if (! alreadyRetrievedDocs.containsKey(id)){
                docsToRetrieve.add(id);
            }
        }
        return docsToRetrieve;
    }

    public static AbstractMap<String, Set<String>> createV2(AbstractMap<String, Set<String>> v1,
                                                            Map<String, Set<String>> mapOfAllV1Docs) throws IOException {

        AbstractMap<String, Set<String>> v2 = new HashMap<>();

        // v2 is a larger version of v1
        v2.putAll(v1);

        // loop through the keys in v2 in order to add the rest of the docs from the mapOfAll
        for (String key : v2.keySet()) {

            // the set to complete with more docs
            Set<String> setOfTheKey = v2.get(key);

            // loop through all docs in the set
            for (String idInOuterSet : setOfTheKey) {
                // get all docs linked to this doc in the v1 set
                Set<String> innerSet = mapOfAllV1Docs.get(idInOuterSet);

                // add to the v1 set
                setOfTheKey.addAll(innerSet);
            }

            // add back (probably not necessary, but I'll let this line be here anyway)
            v2.put(key,setOfTheKey);
        }

        return v2;
    }
    public static String parametersToString(AbstractMap<String, Double> fcnParams){
        String settingsAsString = "";
        String stringToAdd = "";
        for (String paramName : fcnParams.keySet()) {
            settingsAsString = settingsAsString + stringToAdd + paramName + "=" + fcnParams.get(paramName);
            stringToAdd = ",";
        }
        return settingsAsString;
    }

    public static AbstractMap<String, Double> createScoredDocs(JsonObject singleSearchResult){
        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            scoredDocs.put(id, document.getJsonNumber("score").doubleValue());
        }

        return scoredDocs;
    }
    public static AbstractMap<String, Set<String>> createV1(JsonObject singleSearchResult){
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
}
