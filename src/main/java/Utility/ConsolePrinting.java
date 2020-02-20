package Utility;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.List;
import java.util.Set;

public class ConsolePrinting {

    public static void printAllDocsInJson(JsonObject json){
        List results = (List) json.getJsonObject("hits").get("hits");
        for (Object nextObject: results) { // assume that all entries are json objects (and not arrays)
            String id = ((JsonObject) nextObject).getString("_id");
            double docScore = ((JsonObject) nextObject).getJsonNumber("_score").doubleValue();
            System.out.println("Doc: " + id + ". Score: "+ docScore);
        }
    }

    public static void printMyRankedList(String whichCase, List<General.Pair> rankedList) throws IOException {
        System.out.println(whichCase);
        int pos = 1;
        for (General.Pair p : rankedList) {
            String id = p.getKey();
            String title = General.retrieveTitleOfDocById(id);
            System.out.println("Position: " + pos + ", Doc: " + title + ", Score: " + p.getValue());
            pos++;
        }
        System.out.println();
    }

    public static void printUnorderedResults(AbstractMap<String, Double> result){
        Set<String> allKeys = result.keySet();
        for (String key : allKeys) {
            System.out.println(key + ": \t" + result.get(key));
        }
    }

    public static void printOrderedResults(AbstractMap<String, Double> result){
        AbstractQueue<General.Pair> orderedResults =  General.orderResults(result);
        General.Pair nextPair;
        while (!orderedResults.isEmpty()) {
            nextPair = orderedResults.poll();
            System.out.println("Doc " + nextPair.getKey() + " with score " + nextPair.getValue());
        }
    }
}
