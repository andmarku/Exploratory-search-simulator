import javax.json.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SimulatorQueryCreator {

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

    static List<String> createAllMasterQueries(int size, int seed) throws IOException {
        return createRandomQueryTerms(size, seed);
    }

    private static List<String> createRandomQueryTerms(int numberOfQueryTerms, int seed) throws IOException {
        List<String> queryTerms = new ArrayList<>();

        // set up REST parameters
        RetrieverParameterCreator params = new RetrieverParameterCreator();
        params.setRestParamsForRandomQueries(numberOfQueryTerms, seed);

        // retrieve results from elastic
        JsonObject retrievedRes = Retriever.searchResultRetriever(params);

        // pick out the titles
        List<String> titles = extractTitles(retrievedRes);

        // select a random word from each title
        List<String> titleAsList;
        for (String title : titles) {
            title = removeStopWords(title);
            titleAsList = splitStringIntoWords(title);
            queryTerms.add(selectRandomStringFromArrayOfStrings(titleAsList, seed));
        }

        return queryTerms;
    }

    public static String removeStopWords(String strToClean){
        // "\\b" is to account for word boundaries, i.e. not replace "his" in "this"
        // the "\\s?" is to suppress optional trailing white space

        // clean common stop words
        Pattern p = Pattern.compile("[" + UtilityStopWords.SIGNS + "]");
        Matcher m = p.matcher(strToClean);
        String strWithoutSigns = m.replaceAll(" ");

        p = Pattern.compile("\\b("+UtilityStopWords.STOPWORDS+")\\b");
        m = p.matcher(strWithoutSigns);
        String strWithoutStopWords = m.replaceAll(" ").trim();

        System.out.println(strWithoutStopWords);
        return strWithoutStopWords;
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

        String[] words = str.split("\\s+");
        return Arrays.asList(words);
    }
}// end of class