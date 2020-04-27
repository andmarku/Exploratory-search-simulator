package Measures;

import Settings.Settings;
import Utility.General;
import java.io.IOException;
import java.util.*;

public class MeasuresWrapper {
    public static AbstractMap<String, AbstractMap<String, Double>> measureAll(Settings settings,
                                                                              Map<String, List<General.Pair>> rankedListsWithSameQuery,
                                                                              AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1) throws IOException {

        AbstractMap<String, AbstractMap<String, Double>> mapOfMeasures = new HashMap<>();

        // Store map with key = value of p, settings, and value = rbCluster score
        // rbc = rbc for all values of p
        mapOfMeasures.put("rbc", runRbClusters(rankedListsWithSameQuery, settings.getValuesOfP(), mapOfAllDocsInAllOfV1));

        // Store map with key = value of p, settings, and value = rbSampling score,
        // rbs = rbs for all combinations of inner and outer p
        mapOfMeasures.put("rbs", runRbSampling(rankedListsWithSameQuery,
                settings.getValuesOfP(),
                settings.getValuesOfInnerP(),
                mapOfAllDocsInAllOfV1));


        // Store map with key = value of p, combo of settings, and with value = rbOverlap score.
        // rbo = rbo all comparisons for the parameter settings
        mapOfMeasures.put("rbo", runRbOverlapOnAllListInSetting(rankedListsWithSameQuery,
                settings.getValuesOfP()));

        return mapOfMeasures;
    }

    public static AbstractMap<String, Double> runRbSampling(Map<String, List<General.Pair>> mapOfRankedLists,
                                                            List<Double> valuesOfOuterP,
                                                            List<Double> valuesOfInnerP,
                                                            AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1 ) throws IOException {
        // map to store all scores for all p in
        AbstractMap<String, Double> scoresAllDocsForAllCombosOfP = new HashMap<>();

        for (String paramCombo : mapOfRankedLists.keySet()) {
            // retrieve all linked documents
            List<List<String>> retrievedAllLinkedDocs = retrieveAllLinkedDocs(mapOfRankedLists.get(paramCombo),
                    mapOfAllDocsInAllOfV1);

            for (Double outerP : valuesOfOuterP) {
                for (Double innerP : valuesOfInnerP) {
                    // score the result list and its linked documents
                    double samplingScore = RankBiasedSampling.runMeasureSingleResult(retrievedAllLinkedDocs, outerP, innerP);

                    // create key for map. s = parameter settings, op = outer parameter, ip = inner parameter
                    String tag = "id=[s=[" + paramCombo + "],op=" + outerP + ",ip=" + innerP +"]" ;

                    // store the scores
                    scoresAllDocsForAllCombosOfP.put(tag, samplingScore);
                }
            }
        }
        return scoresAllDocsForAllCombosOfP;
    }

    public static AbstractMap<String, Double> runRbClusters(Map<String, List<General.Pair>> rankedListsWithSameQuery,
                                                            List<Double> valuesOfP,
                                                            AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1) throws IOException {
        // map to store all scores for all p in
        AbstractMap<String, Double> rbcScoresAllDocsForAllP = new HashMap<>();

        for (String paramCombo : rankedListsWithSameQuery.keySet()) {
            // retrieve all linked documents
            List<List<String>> retrievedAllLinkedDocs = retrieveAllLinkedDocs(rankedListsWithSameQuery.get(paramCombo),
                    mapOfAllDocsInAllOfV1);

            for (Double p : valuesOfP) {
                // score the result list and its linked documents
                double clusterScore = RankBiasedClusters.runMeasureSingleResult(retrievedAllLinkedDocs, p);

                // key to use in map. s = parameter settings
                String tag = "id=[s=[" + paramCombo + "],p=" + p + "]";

                // store the scores in map
                rbcScoresAllDocsForAllP.put(tag, clusterScore);
            }
        }
        return rbcScoresAllDocsForAllP;
    }

    public static AbstractMap<String, Double> runRbOverlapOnAllListInSetting(Map<String, List<General.Pair>> sims,
                                                                       List<Double> valuesOfP){
        // map to store all scores for all p in
        AbstractMap<String, Double> rboForAllListsForAllP = new HashMap<>();

        // compare each set-up once with each other set-up
        for (String firstKey : sims.keySet()) {
            for (String secondKey : sims.keySet()) {
                // OBS makes sure that each comparison is only done once. (breaks when reaching the matrix diagonal)
                if (firstKey.equals(secondKey)){
                    break;
                }

                for (Double p : valuesOfP) {
                    // compute the score
                    double score = RankBiasedOverlap.computeRankBiasedOverlap(sims.get(firstKey), sims.get(secondKey), p);

                    // create key using CSV format. os = outer parameter settings, is = inner parameter settings.
                    String tag = "id=[os=[" + firstKey + "],is=[" + secondKey + "],p=" + p + "]";

                    // save the score in map
                    rboForAllListsForAllP.put(tag, score);
                }
            }
        }
        return rboForAllListsForAllP;
    }

    public static List<List<String>> retrieveAllLinkedDocs(List<General.Pair> allResults,
                                                           AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1) throws IOException {

        List<List<String>> docsLinkedToTheRankedDocs = new ArrayList<>();

        // go through the (assumed ordered) list of ranked docs
        for (General.Pair pair: allResults) {
            List<String> linkedDocs = new ArrayList<>();

            // add all linked docs from the map of all
            linkedDocs.addAll(mapOfAllDocsInAllOfV1.get(pair.getKey()));

            // append the linked docs (in the right place)
            docsLinkedToTheRankedDocs.add(linkedDocs);
        }

        return docsLinkedToTheRankedDocs;
    }

}
