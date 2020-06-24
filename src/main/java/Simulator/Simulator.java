package Simulator;

import Measures.MeasuresWrapper;
import Retriever.NewParser;
import Retriever.NewRetriever;
import Retriever.RetrieverParser;
import Retriever.RetrieverWrapper;
import Settings.Settings;
import Utility.General;
import Utility.FileWriter;
import org.elasticsearch.search.SearchHits;

import javax.json.*;
import java.io.IOException;
import java.util.*;

public class Simulator {

    public static void runAllIterations(Settings settings, List<String> listOfQueryTerms) throws Exception {

        for (int itr= 0; itr < settings.getNumOfItr(); itr++) {
            if (itr < 26){
                continue;
            }
            /* --- Querying Elastic --- */
            // pick out this iterations query,
            List<String> query = listOfQueryTerms.subList(itr*settings.getSizeOfQuery(), (itr+1)*settings.getSizeOfQuery());
            // retrieve the search result lists corresponding this iterations query

            JsonObject singleSearchResult = RetrieverWrapper.retrieveSearchResults(query, settings.getSizeOfRetrievedList());

            String itrId = "" + itr;

            System.out.println(itrId + "\t" + settings.getSizeOfQuery());
            
            // Prepare for applying the expansion component
            AbstractMap<String, Double> scoredDocs = createScoredDocs(singleSearchResult);
            AbstractMap<String, Set<String>> v1 = createIncompleteV1(singleSearchResult);
            AbstractMap<String, Double> allOfV1Scored = retrieveAllRelevantScores(query, v1);


            // sum each vicinity score and measure the vicinities sizes
            Map<String, Map<String,Double>> preppedDocs = FeatureExpander.prepForExpansion(allOfV1Scored, v1);

            // For each different combination of parameter values, apply component, apply metrics, and store results
            Map<String, List<General.Pair>> mapOfAllListsFromIteration = new HashMap<>();
            List<General.Pair> allTopRankedResults = new ArrayList<>();
            // loop through list of list
            for (List<Double> fcnParams : settings.getParamCombs() ) {

                // Rescore wtr to expansion
                AbstractMap<String, Double> expandedResults = FeatureExpander.expandRanking(preppedDocs,fcnParams);

                // order the scored docs
                List<General.Pair> rankedResults = General.listRankedResults(expandedResults, settings.getMaxSizeOfFinalList());

                allTopRankedResults.addAll(rankedResults);

                // create key for storing results
                String paramName = parametersToString(fcnParams);

                // store results from this param combo
                mapOfAllListsFromIteration.put(paramName, rankedResults);
            }

            AbstractMap<String, Set<String>> completeV0 = createCompleteV0(singleSearchResult);
            AbstractMap<String, Set<String>> mapOfAllTopDocs = createMapOfAllTopDocs(allTopRankedResults, completeV0);

            // score the results from this query
            AbstractMap<String, AbstractMap<String, Double>> mapOfMeasures = MeasuresWrapper.measureAll(settings,
                    mapOfAllListsFromIteration, mapOfAllTopDocs);

//            AbstractMap<String, Double> rbOverlap = mapOfMeasures.get("rbo");
//            FileWriter.writeCsv(itrId, Integer.toString(settings.getSizeOfQuery()), rbOverlap, settings.getRbOverlapPath());

            AbstractMap<String, Double> rbCluster = mapOfMeasures.get("rbc");
            FileWriter.writeCsv(itrId, Integer.toString(settings.getSizeOfQuery()), rbCluster, settings.getRbClusterPath());

            AbstractMap<String, Double> rbSampling = mapOfMeasures.get("rbs");
            FileWriter.writeCsv(itrId, Integer.toString(settings.getSizeOfQuery()), rbSampling, settings.getRbSamplingPath());
        }
    }


    public static AbstractMap<String, Double>  retrieveAllRelevantScores(List<String> queryTerms,
                                                                         AbstractMap<String, Set<String>> completeV0) throws IOException {
        Set<String> ids = completeV0.keySet();
        List<String> idsList = new ArrayList<>();
        idsList.addAll(ids);
        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        for (int i = 0; i < idsList.size(); i ++) {
            int endIndex = i + 1;
            if(endIndex >= idsList.size()){
                endIndex = idsList.size() - 1;
            }
            // query elastic
            SearchHits hits = NewRetriever.queryElastic2(queryTerms, idsList.subList(i, endIndex), idsList.subList(i, endIndex).size());

            // parse hits into json
            JsonObject searchResultsAsJson = NewParser.newParser(hits);

            AbstractMap<String, Double> tempMap = createMapOfScoreSForAllDocs(searchResultsAsJson,100);

            for (String id : tempMap.keySet()) {
                if (!scoredDocs.containsKey(id)){
                    scoredDocs.put(id, tempMap.get(id));
                }else{
                    if (scoredDocs.get(id) < tempMap.get(id)){
                        scoredDocs.put(id, tempMap.get(id));
                    }
                }
            }
        }

        return scoredDocs;
    }



    public static AbstractMap<String, Set<String>> createIncompleteV1(JsonObject singleSearchResult) throws Exception {
        AbstractMap<String, Set<String>> v1 = new HashMap<>();


        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            Set<String> v1Set = new HashSet<>();
            v1Set.add(id);

            JsonArray linkedDocs = document.getJsonArray("citations");
            for (JsonValue jsVal : linkedDocs) {
                //hack to remove citation marks
                String linkedId = RetrieverParser.removeCitationMarks(jsVal.toString());
                v1Set.add(linkedId);

                // add linkedId as its own doc
                if (! v1.containsKey(linkedId)){
                    Set<String> linkedIdSet = new HashSet<>();
                    linkedIdSet.add(linkedId);
                    linkedIdSet.add(id);
                    v1.put(linkedId, linkedIdSet);
                }else{
                    Set<String> linkedIdSet = v1.get(linkedId);
                    linkedIdSet.add(linkedId);
                    linkedIdSet.add(id);
                    v1.put(linkedId, linkedIdSet);
                }
            }
            v1.put(id, v1Set);
        }

        return v1;
    }

    public static AbstractMap<String, Set<String>> createCompleteV0(JsonObject singleSearchResult) throws Exception {
        AbstractMap<String, Set<String>> v0 = new HashMap<>();

        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            Set<String> v1Set = new HashSet<>();
            v1Set.add(id);

            // add all linked docs to the v0set
            JsonArray linkedDocs = document.getJsonArray("citations");
            for (JsonValue jsVal : linkedDocs) {
                //hack to remove citation marks
                String linkedId = RetrieverParser.removeCitationMarks(jsVal.toString());
                v1Set.add(linkedId);
            }
            v0.put(id, v1Set);
        }

        return v0;
    }

    public static AbstractMap<String, Set<String>> createMapOfAllTopDocs(List<General.Pair> allTopResults,
                                                                    AbstractMap<String, Set<String>> docsAlreadyRetrieved) throws Exception {

        AbstractMap<String, Set<String>> mapOfAllTopDocs = new HashMap<>();

        Set<String> docsToRetrieveAsSet = new HashSet<>();
        for (General.Pair p : allTopResults) {
            docsToRetrieveAsSet.add(p.getKey());
        }

        // Docs to retrieve
        List<String> docsToRetrieveAsList = new ArrayList<>();
        for (String linkedId: docsToRetrieveAsSet) {
            // if the linked doc was not in the original hits (or has been linked to before?)
            if (docsAlreadyRetrieved.containsKey(linkedId)) {
                mapOfAllTopDocs.put(linkedId, docsAlreadyRetrieved.get(linkedId));
            }else{
                docsToRetrieveAsList.add(linkedId);
            }
        }

        // retrieve the missing documents
        AbstractMap<String, List<String>> retrievedDocs = NewRetriever.multiGetList(docsToRetrieveAsList);

        // add as set
        for (String linkedId: retrievedDocs.keySet()) {
            Set<String> newSet = new HashSet<>();
            newSet.add(linkedId);
            newSet.addAll(retrievedDocs.get(linkedId));
            mapOfAllTopDocs.put(linkedId, newSet);
        }
        return mapOfAllTopDocs;
    }

    public static AbstractMap<String, Double> createScoredDocs(JsonObject singleSearchResult){
        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            scoredDocs.put(id, document.getJsonNumber("score").doubleValue());
        }
        return scoredDocs;
    }

    public static AbstractMap<String, Double> idAndScoreOfBestHit(JsonObject singleSearchResult){
        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        String idOfDocWithMaxScore = "";
        double maxScore = 0;
        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            if (maxScore < document.getJsonNumber("score").doubleValue()) {
                maxScore = document.getJsonNumber("score").doubleValue();
                idOfDocWithMaxScore = id;
            }
        }
        scoredDocs.put(idOfDocWithMaxScore, maxScore);
        return scoredDocs;
    }

    public static AbstractMap<String, Double> createMapOfScoreSForAllDocs(JsonObject singleSearchResult, double divideWith){
        AbstractMap<String, Double> scoredDocs = new HashMap<>();
        for (String id : singleSearchResult.keySet()) {
            JsonObject document = singleSearchResult.getJsonObject(id);
            double score = document.getJsonNumber("score").doubleValue();
            score = score /divideWith;
            scoredDocs.put(id, score);
        }
        return scoredDocs;
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
