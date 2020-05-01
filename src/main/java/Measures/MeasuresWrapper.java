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

        // Compute the clusters for each list (placed here in order to just do it once)
        AbstractMap<String, List<Integer>> mapOfNrOfClusterForEachSetting =
                computeNrOfClustersForEachSetting(rankedListsWithSameQuery, mapOfAllDocsInAllOfV1);

        // Store map with key = value of p, settings, and value = rbCluster score
        // rbc = rbc for all values of p
        mapOfMeasures.put("rbc", runRbClusters(mapOfNrOfClusterForEachSetting, settings.getValuesOfP()));

        // Store map with key = value of p, settings, and value = rbSampling score,
        // rbs = rbs for all combinations of inner and outer p
        mapOfMeasures.put("rbs", runRbSampling(mapOfNrOfClusterForEachSetting, settings.getValuesOfP(), settings.getValuesOfInnerP()));

        // Store map with key = value of p, combo of settings, and with value = rbOverlap score.
        // rbo = rbo all comparisons for the parameter settings
        mapOfMeasures.put("rbo", runRbOverlapOnAllListInSetting(rankedListsWithSameQuery, settings.getValuesOfP()));

        return mapOfMeasures;
    }

    public static AbstractMap<String, Double> runRbClusters(AbstractMap<String, List<Integer>> mapOfNrOfClusterForEachSetting,
                                                            List<Double> valuesOfP){
        // map to store all scores for all p in
        AbstractMap<String, Double> rbcScoresAllDocsForAllP = new HashMap<>();

        for (String paramCombo : mapOfNrOfClusterForEachSetting.keySet()) {
            for (double p : valuesOfP) {
                // score the result list and its linked documents
                double clusterScore = RankBiasedClusters.computeRankBiasedClusters(mapOfNrOfClusterForEachSetting.get(paramCombo), p);

                // round the score
                clusterScore = Math.floor(clusterScore * 10000) / (double) 10000;

                // key to use in map. s = parameter settings
                String tag = paramCombo + "," + p;

                // store the scores in map
                rbcScoresAllDocsForAllP.put(tag, clusterScore);
            }
        }
        return rbcScoresAllDocsForAllP;
    }

    public static AbstractMap<String, Double> runRbSampling(AbstractMap<String, List<Integer>> mapOfNrOfClusterForEachSetting,
                                                            List<Double> valuesOfOuterP,
                                                            List<Double> valuesOfInnerP){
        // map to store all scores for all p in
        AbstractMap<String, Double> scoresAllDocsForAllCombosOfP = new HashMap<>();

        for (String paramCombo : mapOfNrOfClusterForEachSetting.keySet()) {
           for (double outerP : valuesOfOuterP) {
                for (double innerP : valuesOfInnerP) {
                    // score the result list and its linked documents
                    double samplingScore = RankBiasedSampling.computeRankBiasedSampling(mapOfNrOfClusterForEachSetting.get(paramCombo), outerP, innerP);

                    // round the score
                    samplingScore = Math.floor(samplingScore * 10000) / (double) 10000;

                    // create key for map. s = parameter settings, op = outer parameter, ip = inner parameter
                    String tag = paramCombo + "," + outerP + "," + innerP ;

                    // store the scores
                    scoresAllDocsForAllCombosOfP.put(tag, samplingScore);
                }
            }
        }
        return scoresAllDocsForAllCombosOfP;
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

                    // round the score
                    score = Math.floor(score * 10000) / (double) 10000;

                    // create key using CSV format. os = outer parameter settings, is = inner parameter settings.
                    String tag = firstKey + "," + secondKey + "," + p;

                    // save the score in map
                    rboForAllListsForAllP.put(tag, score);
                }
            }
        }
        return rboForAllListsForAllP;
    }

    public static AbstractMap<String, List<Integer>> computeNrOfClustersForEachSetting(Map<String, List<General.Pair>> rankedListsWithSameQuery,
                                                                                       AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1){
        AbstractMap<String, List<Integer>> nrOfClustersForEachSetting = new HashMap<>();

        for (String paramCombo : rankedListsWithSameQuery.keySet()) {
            // retrieve all linked documents
            List<Set<String>> listOfLinks = getAllLinkedDocs(rankedListsWithSameQuery.get(paramCombo),
                    mapOfAllDocsInAllOfV1);

            // compute number of clusters
            List<Integer> listOfNrOfClustersForSingleSetting = findNumberOfClustersInListForDifferentDepth(listOfLinks);

            // store the scores in map
            nrOfClustersForEachSetting.put(paramCombo, listOfNrOfClustersForSingleSetting);
        }

        return nrOfClustersForEachSetting;
    }

    public static List<Set<String>> getAllLinkedDocs(List<General.Pair> allResults,
                                                           AbstractMap<String, Set<String>> mapOfAllDocsInAllOfV1) {

        List<Set<String>> docsLinkedToTheRankedDocs = new ArrayList<>();

        // go through the (assumed ordered) list of ranked docs
        for (General.Pair pair: allResults) {
            // append the linked docs (in the right place)
            docsLinkedToTheRankedDocs.add(mapOfAllDocsInAllOfV1.get(pair.getKey()));
        }

        return docsLinkedToTheRankedDocs;
    }

    public static List<Integer> findNumberOfClustersInListForDifferentDepth(List<Set<String>> orderedList) {
        List<Integer> allNumbersOfClusters = new ArrayList<>();
        List<Set<Integer>> indicesOfOverlappingSets = new ArrayList<>();

        // start the loop second entry since just one set is always disjoint
        Set<Integer> newCluster = new HashSet<>();
        newCluster.add(0);
        indicesOfOverlappingSets.add(newCluster);
        for (int d = 1; d < orderedList.size(); d++) {
             // find the new list of disjoint sets
             indicesOfOverlappingSets = findAllDisjointSets(orderedList.subList(0, d+1), indicesOfOverlappingSets);

            // add the number of disjoint clusters
            allNumbersOfClusters.add(indicesOfOverlappingSets.size());
        }
        return allNumbersOfClusters;
    }

    public static List<Set<Integer>> findAllDisjointSets(List<Set<String>> allSets, List<Set<Integer>> indicesOfOverlappingSets){
        Stack<Integer> clustersToJoin = new Stack<>();
        Boolean breakLoop = false;
        // find out which clusters that the new set overlaps
        for (int i = 0; i < indicesOfOverlappingSets.size(); i++) {
            breakLoop = false;
            // for each set in the cluster at position i
            for (Integer indexOfSetInCluster : indicesOfOverlappingSets.get(i)) {
                Set<String> existingSet = allSets.get(indexOfSetInCluster);
                // check if the new set overlaps the existing set for any id
                for (String id : allSets.get(allSets.size() - 1)) {
                    if (existingSet.contains(id)){
                        // keep track of the clusters that the new set overlaps
                        clustersToJoin.push(i);
                        breakLoop = true;
                        break;
                    }
                }
                if (breakLoop){
                    break;
                }
            }
        }

        // if the new set did not overlap any old clusters, add it as a cluster on its own
        // merge the clusters that have become joined
        if (clustersToJoin.isEmpty()){
            Set<Integer> newCluster = new HashSet<>();
            newCluster.add(allSets.size() - 1);
            indicesOfOverlappingSets.add(newCluster);
        }else{
            Set<Integer> newCluster = new HashSet<>();
            newCluster.add(allSets.size() - 1);
            while (!clustersToJoin.isEmpty()){
                int i = clustersToJoin.pop();
                newCluster.addAll(indicesOfOverlappingSets.get(i));
                indicesOfOverlappingSets.remove(i);
            }
            indicesOfOverlappingSets.add(newCluster);
        }

        return indicesOfOverlappingSets;
    }
}
