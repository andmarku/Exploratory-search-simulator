import javax.json.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Simulator {
    private static int sizeOfFullQuery = 2;
    private static int numOfItr;
    private static int numOfSubQueries = setNumOfSubQueries(2, sizeOfFullQuery); // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
    private static int sizeOfFinalRankedList = 10;
    private static int sizeOfRetrievedList = 10;
    private static double expansionMultiplier = 0.5;

    // TODO: 2019-10-30 general observation: the ranking score from elastic is not normalized. i need to do this myself when combining
    public static void moreGeneralSimulator() throws IOException {
        // GENERAL SET-UP
        numOfItr = 1;
        sizeOfFullQuery = 2;
        sizeOfFinalRankedList = 10;

        /* SIMULATION - set-up */
        // create the master query
        List<String> masterQuery = QueryCreator.createMasterQuery(sizeOfFullQuery, 10);

        /* SIMULATION - base case */
        numOfSubQueries = 1; // just one query in the base case
        sizeOfRetrievedList = 10;
        expansionMultiplier = 0;

        List<UtilityFunctions.Pair> listedRankedResults_base = produceRankedList(masterQuery);

        System.out.println("The base case");
        int pos = 1;
        for (UtilityFunctions.Pair p : listedRankedResults_base) {
            System.out.println("Position: " + pos + ", Doc: " + p.getKey() + ", Score: " + p.getValue());
            pos++;
        }
        System.out.println("");

        /* SIMULATION - trial case */
        numOfSubQueries = setNumOfSubQueries(2, sizeOfFullQuery); // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        sizeOfRetrievedList = 10;
        expansionMultiplier = 0.5;

        List<UtilityFunctions.Pair> listedRankedResults_trial = produceRankedList(masterQuery);

        System.out.println("The trial case");
        pos = 1;
        for (UtilityFunctions.Pair p : listedRankedResults_trial) {
            System.out.println("Position: " + pos + ", Doc: " + p.getKey() + ", Score: " + p.getValue());
            pos++;
        }
    }// end of moreGeneralSimulator

    public static List<UtilityFunctions.Pair> produceRankedList
            (List<String> masterQuery) throws IOException {
        // segment the query, if applicable
        // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        List<List<String>> subQueries = QueryCreator.segmentQuery(masterQuery, sizeOfFullQuery, numOfSubQueries);

        // Rank expanded hits for each query in the list
        List<AbstractMap<String, Double>> storedRankedLists = rankAllQueries(subQueries, sizeOfRetrievedList, expansionMultiplier);

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
//            JsonObject retrievedRes = Retriever.searchResultRetriever(query, sizeOfRetrievedList);

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

    public static void mySimulator() throws IOException {
        int sizeOfRetrievedList = 10;

        String query = UrlCreator.formatQueryForUrl(QueryCreator.createStaticQuery());
        URL url = UrlCreator.createSearchUrlFromString(query, sizeOfRetrievedList);
        JsonObject retrievedRes = Retriever.searchResultRetriever_firstAttempt(url);
        List expandedRes = SearchDocParser.docAndLinksScoreParser(retrievedRes);
        AbstractMap<String, Double> results = QueryExpander.docAndPercentageLinkScorer((HashMap<String, Double>) expandedRes.get(0),
                (HashMap<String, Double>) expandedRes.get(1), (Double) expandedRes.get(2), 1);
        System.out.println("Original ranked list");
        UtilityFunctions.printAllDocsInJson(retrievedRes);
        System.out.println("My ranked list");
        UtilityFunctions.printOrderedResults(results);
    }
}// end of simulator class
