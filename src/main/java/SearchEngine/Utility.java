package SearchEngine;

import Retriever.ParameterCreator;
import Retriever.Parser;
import Retriever.Retriever;
import Utility.General;

import javax.json.JsonObject;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utility {

    public static List<General.Pair> produceRankedListFromBaseQuery
            (List<List<String>> queries, int sizeOfRetrievedList, int sizeOfFinalRankedList) throws Exception {

        // Rank expanded hits for the query
        List<AbstractMap<String, Double>> storedRankedLists = rankAllQueries(
                queries, sizeOfRetrievedList, 0);

        // pick out the first x of the scored docs
        return General.listRankedResults(storedRankedLists.get(0), sizeOfFinalRankedList);
    }

    public static List<General.Pair> produceRankedListFromListOfQueries
            (List<List<String>> queries, double expansionMultiplier,
             int sizeOfRetrievedList, int sizeOfFinalRankedList) throws Exception {

        // Rank expanded hits for each query in the list
        List<AbstractMap<String, Double>> storedRankedLists = rankAllQueries(
                queries, sizeOfRetrievedList, expansionMultiplier);

        // combine all sub-queries into a final list
        AbstractMap<String, Double> scoredResults = FeatureCombiner.multiplierCombiner(storedRankedLists);

        // pick out the first x of the scored docs
        return General.listRankedResults(scoredResults, sizeOfFinalRankedList);
    }

    /*
     *  Ranks expanded hits for each query in the list
     */
    private static List<AbstractMap<String, Double>> rankAllQueries
    (List<List<String>> queries, int sizeOfRetrievedList, double expansionMultiplier) throws Exception {

        List<AbstractMap<String, Double>> storedRankedLists = new ArrayList<>();
        ParameterCreator queryParams = new ParameterCreator();

        for (List<String> query : queries) {
            // search elastic for the specific query
            queryParams.setRestParamsForStandardQuery(query, sizeOfRetrievedList);
            JsonObject retrievedRes = Retriever.searchResultRetriever(queryParams);

            // parse the elastic ranked list into scoredDocs, linkedDocs, and totalScore
            List parsedResult = Parser.docAndLinksScoreParser(retrievedRes);
            AbstractMap<String, Double> scoredDocs = (HashMap<String, Double>) parsedResult.get(0);
            AbstractMap<String, Double> linkedDocs = (HashMap<String, Double>) parsedResult.get(1);
            double totalScore = (Double) parsedResult.get(2);

            // Rescore wtr to expansion
            AbstractMap<String, Double> scoredResults =
                    FeatureExpander.docAndPercentageLinkScorer(scoredDocs, linkedDocs, totalScore, expansionMultiplier);

            // store
            storedRankedLists.add(scoredResults);
        }

        return storedRankedLists;
    }



}