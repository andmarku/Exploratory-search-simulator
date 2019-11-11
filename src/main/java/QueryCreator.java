import javax.json.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
class QueryCreator {

    static List<String> createMasterQuery(int sizeOfFullQuery, int seed) throws IOException {
        return createRandomQueryTerms(sizeOfFullQuery, seed);
    }

    /*
    * assumes that the query is divisible by the number of subqueries
    * */
    static List<List<String>> segmentQuery(List<String> masterQuery, int sizeOfFullQuery, int numOfSubQueries){
        int sizeOfSubQueries = sizeOfFullQuery/numOfSubQueries;
        List<List<String>> queries = new ArrayList<>();
        for(int i = 0; i<sizeOfFullQuery; i = i + sizeOfSubQueries) {
            queries.add(masterQuery.subList(i, i + sizeOfSubQueries));
        }
        return queries;
    }


    private static List<String> createRandomQueryTerms(int numberOfQueryTerms, int seed) throws IOException {
        List<String> queryTerms = new ArrayList<>();

        // set up REST parameters
        RestParameterCreator params = new RestParameterCreator();
        params.setRestParamsForRandomQueries(numberOfQueryTerms, seed);

        // retrieve results from elastic
        JsonObject retrievedRes = NewRetriever.searchResultRetriever(params);

        // pick out the titles
        List<String> titles = extractTitles(retrievedRes);

        // select a random word from each title
        List<String> titleAsList;
        for (String title : titles) {
            titleAsList = splitStringIntoWords(title);
            queryTerms.add(selectRandomStringFromArrayOfStrings(titleAsList, seed));
        }

        return queryTerms;
    }

    // assumes that titles are not null
    static List<String> extractTitles(JsonObject docs){
        List<String> titles = new ArrayList<>();

        // pick out the list of retrieved documents
        List results = (List) docs.getJsonObject("hits").get("hits");

        for (Object nextObject: results) {
            // assume that all entries are json objects (and not arrays)
            JsonObject nextDoc = (JsonObject) nextObject;

            String title = nextDoc.getJsonObject("_source").getString("title");
            titles.add(title);
        }// end of loop through all retrieved documents
        return titles;
    }

    private static String selectRandomStringFromArrayOfStrings(List<String> myList, int seed){
        if( myList.size() == 0){
            return "";
        }
        Random randomGenerator = new Random(seed);
        int index = randomGenerator.nextInt(myList.size());
        return myList.get(index);
    }

    private static List<String> splitStringIntoWords(String str){
        //if string is empty or null, return empty array
        if(str == null || str.equals("")) {
            return new ArrayList<>();
        }

        String[] words = str.split(" ");
        return Arrays.asList(words);
    }
}// end of class
