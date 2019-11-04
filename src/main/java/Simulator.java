import javax.json.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Simulator {

    // TODO: 2019-11-04 double check all variables and stuff so that I'm really sending in the correct values

    // TODO: 2019-11-04 factor out all references to old retriever, + lots of methods eg in simulator

    // TODO: 2019-10-30 general observation: the ranking score from elastic is not normalized.
    //  i need to do this myself when combining
    public static void moreGeneralSimulator() throws IOException {
        /*----------------------------*/
        /* --- GENERAL SET-UP --- */
        /*----------------------------*/
        // list to store results in
        List<JsonObject> allSimulationResults = new ArrayList<>();

        // path to store result in
        String pathToFolder = "C:\\Devlopment\\";
        String simulationName = "firstAttempt";

        // general parameters
        int numOfItr = 1;
        // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        int sizeOfFullQuery = 2;
        int sizeOfFinalRankedList = 10;
        int sizeOfRetrievedList = 10;

        // base case
        double baseCaseExpansionMultiplier = 0;
        int baseCaseNumOfSubQueries = 1;

        // trial case
        double expansionMultiplier = 2;
        // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        int numOfSubQueries = setNumOfSubQueries(2, sizeOfFullQuery);


        /*----------------------------*/
        /* --- SIMULATION: set-up --- */
        /*----------------------------*/
        // store settings
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        JsonObject settings = JsonPrinter.createJsonObjectFromSettings(
                date, sizeOfFullQuery, numOfItr, sizeOfFinalRankedList,
                sizeOfRetrievedList, numOfSubQueries, baseCaseNumOfSubQueries,
                expansionMultiplier, baseCaseExpansionMultiplier);
        allSimulationResults.add(settings);

        // create the master query
        List<String> masterQuery = QueryCreator.createMasterQuery(sizeOfFullQuery, 10);


        /*----------------------------*/
        /* --- SIMULATION: base case --- */
        /*----------------------------*/
        List<UtilityFunctions.Pair> listedRankedResults_base = produceRankedList(
                masterQuery,baseCaseExpansionMultiplier, baseCaseNumOfSubQueries,
                sizeOfFullQuery, sizeOfRetrievedList, sizeOfFinalRankedList);

        // printing to the console for testing
        printMyRankedList("The base case", listedRankedResults_base);


        /*----------------------------*/
        /* --- SIMULATION: trial case --- */
        /*----------------------------*/
        List<UtilityFunctions.Pair> listedRankedResults_trial = produceRankedList(
                masterQuery, expansionMultiplier, numOfSubQueries, sizeOfFullQuery,
                sizeOfRetrievedList, sizeOfFinalRankedList);

        // printing to the console for testing
        printMyRankedList("The trial case", listedRankedResults_trial);


        /*----------------------------*/
        /* --- END of ITERATION: storing --- */
        /*----------------------------*/
        allSimulationResults.add(
                JsonPrinter.createJsonObjectFromTwoResults(
                        listedRankedResults_base,
                        listedRankedResults_trial));


        /*----------------------------*/
        /* --- END OF SIMULATION: storing --- */
        /*----------------------------*/
        storeResults(allSimulationResults, pathToFolder, simulationName);

    }// end of moreGeneralSimulator

    public static void printMyRankedList(String whichCase, List<UtilityFunctions.Pair> rankedList){
        System.out.println(whichCase);
        int pos = 1;
        for (UtilityFunctions.Pair p : rankedList) {
            System.out.println("Position: " + pos + ", Doc: " + p.getKey() + ", Score: " + p.getValue());
            pos++;
        }
        System.out.println("");
    }

    public static void storeResults(List<JsonObject> results, String pathToFolder, String simulationName)
            throws FileNotFoundException {
        String path = JsonPrinter.createCompleteFileName(pathToFolder, simulationName);
        JsonPrinter.myFileWriter( results,path);
    }

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
