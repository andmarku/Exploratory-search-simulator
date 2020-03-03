package Simulator;

import Retriever.ParameterCreator;
import Retriever.Retriever;
import Settings.Settings;
import Utility.JsonCreator;
import Utility.FileWriter;

import javax.json.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryCreator {
    static void querySimulator(Settings settings) throws Exception {
        // create a seed for repeatable simulations
        int seed = (int) (Math.random()*100);
        System.out.println("Seed for generating queries " + seed + ", making " +  settings.getNumOfItr() * settings.getSizeOfFullQuery() + " query terms");

        // create queries
        List<String> listOfQueryTerms = QueryCreator.createAllMasterQueries(settings.getNumOfItr() * settings.getSizeOfFullQuery(), seed);

        // turn queries into json
        JsonArray queriesAsJson = JsonCreator.createJsonArrayFromListOfStrings(listOfQueryTerms);

        // store in the file specified in the settings
        storeQueries(settings, queriesAsJson);
    }

    /*
     * Assumes that the query is divisible by the number of subqueries
     * */
    public static List<List<String>> segmentQuery(List<String> masterQuery, int sizeOfFullQuery, int numOfSubQueries){
        int sizeOfSubQueries = sizeOfFullQuery/numOfSubQueries;
        List<List<String>> queries = new ArrayList<>();
        for(int i = 0; i<sizeOfFullQuery; i = i + sizeOfSubQueries) {
            queries.add(masterQuery.subList(i, i + sizeOfSubQueries));
        }
        return queries;
    }

    public static List<String> createAllMasterQueries(int size, int seed) throws IOException {
        return createRandomQueryTerms(size, seed);
    }

    private static List<String> createRandomQueryTerms(int numberOfQueryTerms, int seed) throws IOException {
        List<String> queryTerms = new ArrayList<>();

        // set up REST parameters
        ParameterCreator params = new ParameterCreator();
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

    private static String removeStopWords(String strToClean){
        // "\\b" is to account for word boundaries, i.e. not replace "his" in "this"
        // the "\\s?" is to suppress optional trailing white space

        // clean common stop words
        Pattern p = Pattern.compile("[" + QueryStopWords.SIGNS + "]");
        Matcher m = p.matcher(strToClean);
        String strWithoutSigns = m.replaceAll(" ");

        p = Pattern.compile("\\b("+ QueryStopWords.STOPWORDS+")\\b");
        m = p.matcher(strWithoutSigns);
        String strWithoutStopWords = m.replaceAll(" ").trim();

        /*System.out.println(strWithoutStopWords);*/
        return strWithoutStopWords;
    }

    /*
    Method for parsing out titles from the results from elastic
    Is currently needed as public in order to use ugly hack in Utility.General.retrieveTitleOfDocById
    OBS! assumes that titles are not null.
     */
    public static List<String> extractTitles(JsonObject docs){
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

    private static void storeQueries(Settings settings, JsonValue jsonsToStore) throws Exception {
        Map<String, JsonValue> storageMap = new HashMap<>();

        // create json for settings
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("date", date)
                .add("sizeOfFullQuery", settings.getSizeOfFullQuery())
                .add("itr", settings.getNumOfItr());

        // add settings to map
        storageMap.put("settings", jsonBuilder.build());

        // add jsons to map
        storageMap.put("queryTerms", jsonsToStore);

        // save map to file as json
        JsonObject jsonToPrint = JsonCreator.createJsonFromMapOfJsons(storageMap);

        // print using the storeResultInFile wrapper
        List<JsonObject> jsonInList = new ArrayList<>();
        jsonInList.add(jsonToPrint);
        FileWriter.storeResultsInFile(jsonInList, settings.getQueryPath());
    }

    public static List<String> parseMasterQueries(JsonObject jsonFromFile){
        JsonArray queriesAsJsonArray = jsonFromFile.getJsonArray("queryTerms");
        List<String> queriesAsList = new ArrayList<>();
        for (int i = 0; i < queriesAsJsonArray.size(); i++) {
            queriesAsList.add(queriesAsJsonArray.getString(i));
        }
        return queriesAsList;
    }
}// end of class
