package Measures;

import Utility.General;

import javax.json.JsonObject;
import java.util.*;

public class MeasuresParser {
    public static AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> parseListOfSimulationResults(List<JsonObject> simsFromFile){
        AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allItr = new HashMap<>();
        AbstractQueue<General.Pair> sortedRes = new PriorityQueue<>();

        int itr;
        for (JsonObject json : simsFromFile) {
            List<General.Pair> listRes = new ArrayList<>();

            // extract which iteration this list is from (should be in order)
            itr = json.getJsonObject("settings").getInt("itr");
            System.out.println(itr);

            // extract which GoP simulation the results belong to
            int em = json.getJsonObject("settings").getInt("eM");
            int nSq = json.getJsonObject("settings").getInt("nSq");;
            String gopKey = "itr=" + itr + ",em=" + em + ",nSq=" + nSq;

            // extract ranked list
            JsonObject rankedList = json.getJsonObject("rankedList");
            for (String key : rankedList.keySet()) {
                General.Pair pair = new General.Pair(key, rankedList.getJsonNumber(key).doubleValue());
                sortedRes.add(pair);
            }

            // turn AbstractQue into List
            while (!sortedRes.isEmpty()){
                listRes.add(sortedRes.poll());
            }

            // see if anything regarding this iteration has been added yet. If not, add an empty list.
            if (!allItr.containsKey(itr)){
                allItr.put(itr, new HashMap<>());
            }

            // add to the list containing each list in the iteration
            allItr.get(itr).put(gopKey, listRes);
        }

        return allItr;
    }
}
