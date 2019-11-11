import javax.json.JsonObject;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulatorUtility {
    public static List<UtilityFunctions.Pair> produceRankedList
            (List<String> masterQuery, double expansionMultiplier, int numOfSubQueries, int sizeOfFullQuery,
             int sizeOfRetrievedList, int sizeOfFinalRankedList)
            throws IOException {

        // segment the query, if applicable
        // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        List<List<String>> subQueries = QueryCreator.segmentQuery(masterQuery, sizeOfFullQuery, numOfSubQueries);

        // Rank expanded hits for each query in the list
        List<AbstractMap<String, Double>> storedRankedLists = rankAllQueries(
                subQueries, sizeOfRetrievedList, expansionMultiplier);

        // combine all subqueries into a final list
        AbstractMap<String, Double> scoredResults = RankedListCombiner.sumQueryResultCombiner(storedRankedLists);

        // pick out the first x of the scored docs
        return UtilityFunctions.listRankedResults(scoredResults, sizeOfFinalRankedList);
    }

    /*
     *  Ranks expanded hits for each query in the list
     */
    public static List<AbstractMap<String, Double>> rankAllQueries
    (List<List<String>> subQueries, int sizeOfRetrievedList, double expansionMultiplier) throws IOException {

        List<AbstractMap<String, Double>> storedRankedLists = new ArrayList<>();
        RestParameterCreator queryParams = new RestParameterCreator();
        for (List<String> query:subQueries) {
            // search elastic for the specific query
            queryParams.setRestParamsForStandardQuery(query, sizeOfRetrievedList);
            JsonObject retrievedRes = NewRetriever.searchResultRetriever(queryParams);

            // parse the elastic ranked list into scoredDocs, linkedDocs, and totalScore
            List parsedResult = SearchDocParser.docAndLinksScoreParser(retrievedRes);
            AbstractMap<String, Double> scoredDocs = (HashMap<String, Double>) parsedResult.get(0);
            AbstractMap<String, Double> linkedDocs = (HashMap<String, Double>) parsedResult.get(1);
            double totalScore = (Double) parsedResult.get(2);

            // Rescore wtr to expansion
            AbstractMap<String, Double> scoredResults =
                    QueryExpander.docAndPercentageLinkScorer(scoredDocs, linkedDocs, totalScore, expansionMultiplier);

            // store
            storedRankedLists.add(scoredResults);
        }
        return storedRankedLists;
    }

    /*
    makes sure that the query is not split into more subqueries than there are words in the larger query
     */
    public static int setNumOfSubQueries(int num, int sizeOfFullQuery){
        if(num > sizeOfFullQuery){
            return sizeOfFullQuery;
        }
        return num;
    }
}
