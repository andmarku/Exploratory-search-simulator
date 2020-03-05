package Measures;

import SearchEngine.SearchEngineWrapper;
import Utility.General;

import java.util.*;

public class PrepClusterMetric {


    public static List<Set<Integer>> createListOfSets(AbstractMap<String, List<General.Pair>> result) throws Exception {
        System.out.println("Adds all setups to same lsit");

        List<String> lsResult = new ArrayList<>();
        for (String key: result.keySet()) {
            for (General.Pair p: result.get(key)) {
                lsResult.add(p.getKey());
            }
        }

        List<List<String>> queries = new ArrayList<>();
        queries.add(lsResult);
        List<AbstractMap<String, AbstractMap<String, Double>>> stuff = SearchEngineWrapper.retrieveClustersLists(queries,10000);
        AbstractMap<String, AbstractMap<String, Double>> stuff_2 = stuff.get(0);
        AbstractMap<String, Double> scoredDocs = stuff_2.get("scoredDocs");
        AbstractMap<String, Double> linkedDocs = stuff_2.get("linkedDocs");

        System.out.println("scoredDocs \n");
        System.out.println(scoredDocs);
        System.out.println("\nlinkedDocs\n");
        System.out.println(linkedDocs);

        List<Set<Integer>> two = null;
        return two;

    }

}
