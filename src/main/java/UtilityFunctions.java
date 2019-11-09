import javax.json.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class UtilityFunctions {

    public static void printOrderedResults(AbstractMap<String, Double> result){
        AbstractQueue<Pair> orderedResults =  orderResults(result);
        Pair nextPair;
        while (!orderedResults.isEmpty()) {
            nextPair = orderedResults.poll();
            System.out.println("Doc " + nextPair.getKey() + " with score " + nextPair.getValue());
        }
    }

    public static List<String> retrieveTitlesOfListWithDocIds(List<String> docIds) throws IOException {
        List<String> titles = new ArrayList<>();

        for (String id: docIds) {
            RestParameterCreator params = new RestParameterCreator();
            params.setRestParamsForSingleId(id);
            JsonObject doc = NewRetriever.searchResultRetriever(params);

            // ugly hack but the search should only return one document which matches the id
            titles.add(QueryCreator.extractTitles(doc).get(0));
        }

        return titles;
    }

    public static String retrieveTitleOfDocById(String docId) throws IOException {
        RestParameterCreator params = new RestParameterCreator();
        params.setRestParamsForSingleId(docId);
        JsonObject doc = NewRetriever.searchResultRetriever(params);

        System.out.println(docId);
        System.out.println(doc);

        // ugly hack but the search should only return one document which matches the id
        return QueryCreator.extractTitles(doc).get(0);
    }

    public static void printUnorderedResults(AbstractMap<String, Double> result){
        Set<String> allKeys = result.keySet();
        for (String key : allKeys) {
            System.out.println(key + ": \t" + result.get(key));
        }
    }

    public static void printAllDocsInJson(JsonObject json){
        // pick out the list of retrieved documents
        List results = (List) json.getJsonObject("hits").get("hits");

        // print all retrieved documents
        for (Object nextObject: results) {
            // assume that all entries are json objects (and not arrays)
            JsonObject nextDoc = (JsonObject) nextObject;

            // get id and score
            String id = nextDoc.getString("_id");
            double docScore = nextDoc.getJsonNumber("_score").doubleValue();

            System.out.println("Doc: " + id + ". Score: "+ docScore);

        }// end of loop through all retrieved documents
    }

    public static AbstractQueue<Pair> orderResults(AbstractMap<String,Double> scoredDocs){
        Set<String> allKeys = scoredDocs.keySet();
        AbstractQueue<Pair> sortedValues = new PriorityQueue<Pair>();

        for (String key: allKeys) {
            sortedValues.add(new Pair(key, scoredDocs.get(key)));
        }

        return sortedValues;
    }//end of orderResults

    public static List<Pair> listRankedResults(AbstractMap<String,Double> scoredDocs, int numOfRankedResults){
        AbstractQueue<Pair> orderedResults = orderResults(scoredDocs);
        List<Pair> listedResults = new ArrayList<>();
        for (int i = 0; i < numOfRankedResults; i++) {
            if (orderedResults.isEmpty()){
                break;
            }
            listedResults.add(orderedResults.poll());
        }
        return listedResults;
    }//end of listRankedResults


    static class Pair implements Comparable<Pair>{
        private String key;
        private Double value;

        public Pair(String key, Double value){
            this.key = key;
            this.value = value;
        }
        @Override
        public int compareTo(Pair o) {
            return (value < o.value)? 1:-1;
        }
        public String getKey(){
            return key;
        }

        public Double getValue(){
            return value;
        }
    } //end of inner class Pair
}// end of class
