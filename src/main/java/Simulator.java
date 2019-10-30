import javax.json.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Simulator {
    private static int sizeOfFullQuery;
    private static int numOfSubQueries = 1; // default using just one query
    private static int numOfItr;
    private static int sizeOfRankedList;
    private static double expansionMultiplier;
    private static QueryResultCombiner rankedListCombiner;

    public static void moreGeneralSimulator() throws IOException {
        // PARAMETERS
        numOfItr = 1;
        sizeOfFullQuery = 4;
        numOfSubQueries = setNumOfSubQueries(1);
        sizeOfRankedList = 10;
        expansionMultiplier = 0.5;
        rankedListCombiner = new QueryResultCombiner().vsm();

        // WILL ALWAYS BE DONE
        // create the master query
        String myQuery = createQuery(sizeOfFullQuery);
        // segment the query, if applicable
        List<String> subQueries = segmentQuery(myQuery);

        // search and re-rank each subquery
        List<PriorityQueue<UtilityFunctions.Pair>> storedRankedLists = new ArrayList<>();
        for (String query:subQueries) {
            // search elastic for the specific query
            String formattedQuery = UrlFactory.formatQueryForUrl(query);
            URL url = UrlFactory.createSearchUrl(query, sizeOfFullQuery);
            JsonObject retrievedRes = Retriever.searchResultRetriever(url);

            // parse the elastic ranked list into scoredDocs, linkedDocs, and totalScore
            List expandedRes = SearchDocParser.docAndLinksScoreParser(retrievedRes);
            AbstractMap<String, Double> scoredDocs = (HashMap<String, Double>) expandedRes.get(0);
            AbstractMap<String, Double> linkedDocs = (HashMap<String, Double>) expandedRes.get(1);
            double totalScore = (Double) expandedRes.get(2);

            // Rescore wtr to expansion importance
            AbstractMap<String, Double> results = Scorer.docAndPercentageLinkScorer(scoredDocs,linkedDocs, totalScore, expansionMultiplier);

            // order the results
            PriorityQueue<UtilityFunctions.Pair> orderRes = orderResults(results);

            // store
            storedRankedLists.add(orderRes);
        }

        // final ranked list
        PriorityQueue<UtilityFunctions.Pair> finalRankedResult;
        if (storedRankedLists.size() > 1){
            finalRankedResult = rankedListCombiner(storedRankedLists);
        }else{
            finalRankedResult = storedRankedLists.get(0);
        }

        // TODO: 2019-10-29  continue 

    }

    public static void mySimulator() throws IOException {
        String query = UrlFactory.formatQueryForUrl(QueryFactory.createStaticQuery());
        URL url = UrlFactory.createSearchUrl(query, sizeOfFullQuery);
        JsonObject retrievedRes = Retriever.searchResultRetriever(url);
        List expandedRes = SearchDocParser.docAndLinksScoreParser(retrievedRes);
        AbstractMap<String, Double> results = Scorer.docAndPercentageLinkScorer((HashMap<String, Double>) expandedRes.get(0),
                (HashMap<String, Double>) expandedRes.get(1), (Double) expandedRes.get(2), 1);
        System.out.println("Original ranked list");
        UtilityFunctions.printAllDocsInJson(retrievedRes);
        System.out.println("My ranked list");
        UtilityFunctions.printOrderedResults(results);


    }
    /*
    makes sure that the query is not split into more subqueries than there are words in the larger query
     */
    public static int setNumOfSubQueries(int num){
        if(num > sizeOfFullQuery){
            return sizeOfFullQuery;
        }
        return num;
    }
}// end of simulator class
