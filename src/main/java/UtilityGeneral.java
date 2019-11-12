import javax.json.JsonObject;
import java.io.IOException;
import java.util.*;

class UtilityGeneral {
    static String retrieveTitleOfDocById(String docId) throws IOException {
        RetrieverParameterCreator params = new RetrieverParameterCreator();
        params.setRestParamsForSingleId(docId);
        JsonObject doc = Retriever.searchResultRetriever(params);

        //System.out.println(doc);
        // ugly hack but the search should only return one document which matches the id
        return SimulatorQueryCreator.extractTitles(doc).get(0);
    }

    static AbstractQueue<Pair> orderResults(AbstractMap<String, Double> scoredDocs){
        Set<String> allKeys = scoredDocs.keySet();
        AbstractQueue<Pair> sortedValues = new PriorityQueue<>();

        for (String key: allKeys) {
            sortedValues.add(new Pair(key, scoredDocs.get(key)));
        }

        return sortedValues;
    }//end of orderResults

    static List<Pair> listRankedResults(AbstractMap<String,Double> scoredDocs, int numOfRankedResults){
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

        Pair(String key, Double value){
            this.key = key;
            this.value = value;
        }
        @Override
        public int compareTo(Pair o) {
            return (value < o.value)? 1:-1;
        }
        String getKey(){
            return key;
        }

        Double getValue(){
            return value;
        }
    } //end of inner class Pair
}// end of class
