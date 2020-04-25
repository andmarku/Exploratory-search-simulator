package Utility;

import Retriever.ParameterCreator;
import Retriever.Retriever;
import QueryCreator.QueryCreator;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.*;

public class General {
    public static String retrieveTitleOfDocById(String docId) throws IOException {
        ParameterCreator params = new ParameterCreator();
        params.setRestParamsForSingleId(docId);
        JsonObject doc = Retriever.searchResultRetriever(params);

        // ugly hack but the search should only return one document which matches the id
        return QueryCreator.extractTitles(doc).get(0);
    }

    public static AbstractQueue<Pair> orderResults(AbstractMap<String, Double> scoredDocs){
        Set<String> allKeys = scoredDocs.keySet();
        AbstractQueue<Pair> sortedValues = new PriorityQueue<>();

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

    public static class Pair implements Comparable<Pair>{
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
