package Measures;

import Retriever.NewRetriever;
import Settings.Settings;
import Utility.FileReader;
import Utility.General;

import javax.json.JsonObject;
import java.util.*;

public class MeasuresWrapper {
    public static void measureAll(Settings settings, List<List<General.Pair>> rankedListsWithSameQuery){
        double p = 0.7;
        System.out.println("OBS!!!! P is set in writing method.");

        // read queries from file
        List<JsonObject> simulationsListOfJsons = FileReader.readJsonFromFile(settings.getSimulationPath());

        // parse jsons read from file
        AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allResults = MeasuresParser.parseListOfSimulationResults(simulationsListOfJsons);

        /////////////////////////////
        AbstractMap<String, AbstractMap<String, Double>> twoScoresAllDocs = runRbcAndRbs(settings, rankedListsWithSameQuery, p);


        /////////////////////////////

        AbstractMap<String, AbstractMap<String, Double>> allRboComparisonsPerQuery = runRboOnAllListInSetting(settings, rankedListsWithSameQuery, p);

        /////////////////////////////
        // save the score with the setting as key
        scoredMeasure.put(key, score);

        // add the scores of the simulations to the main map
        scoredList.put(simNr, scoredSettings);
    }

    public static AbstractMap<String, Double> runRboOnAllListInSetting(){
        AbstractMap<String, Double> rboForAllLists = new HashMap<>();

        // compare each set-up once with each other set-up
        for (String firstKey : sims.keySet()) {
            for (String secondKey : sims.keySet()) {
                // OBS makes sure that each comparison is only done once. (breaks when reaching the matrix diagonal)
                if (firstKey.equals(secondKey)){
                    break;
                }
                // compute the score
                double score = RankBiasedOverlap.computeRankBiasedDistance(sims.get(firstKey), sims.get(secondKey), p);

                // save the score in map using CSV format
                String name = firstKey + "," + secondKey;
                rboForAllLists.put(name, score);
            }
        }
        return rboForAllLists;
    }


    public static AbstractMap<String, AbstractMap<String, Double>> runRbcAndRbs(Settings settings, List<List<General.Pair>> rankedListsWithSameQuery){
        // map to store all scores in (outer key is type of score, inner is docId)
        AbstractMap<String, AbstractMap<String, Double>> twoScoresAllDocs = new HashMap<>();

        // alternativt
/*
        // score the result list and its linked documents
        double score = RankBiasedClusters.runMeasureSingleResult(retrievedAllLinkedDocs, p);
*/

        // through all combinations of params
        for ( List<General.Pair> singleRes : rankedListsWithSameQuery) {
            AbstractMap<String, Double> scoredMeasure = new HashMap<>();

            // retrieve all linked documents
            List<List<String>> retrievedAllLinkedDocs;

            // score the result list and its linked documents
            double clusterScore = RankBiasedClusters.runMeasureSingleResult(retrievedAllLinkedDocs, p);
            scoredMeasure.put("cluster", clusterScore);

            // score the result list and its linked documents
            double samplingScore = RankBiasedSampling.runMeasureSingleResult(retrievedAllLinkedDocs, p);

            // save the score with the setting as key
            scoredMeasure.put("sampling", samplingScore);

            twoScoresAllDocs.put(settingsKey, scoredMeasure);
        }

        return twoScoresAllDocs;
    }























    /*public static void run2(Settings settings) throws Exception {
        // OBS!!
        double p = 0.7;
        System.out.println("OBS!!!! P is set in writing method.");

        // read queries from file
        List<JsonObject> simulationsListOfJsons = FileReader.readJsonFromFile(settings.getSimulationPath());

        // parse jsons read from file
        AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allResults = MeasuresParser.parseListOfSimulationResults(simulationsListOfJsons);

        // map to store all scores in
        AbstractMap<Integer, AbstractMap<String, Double>> scoredSimulations = new HashMap<>();

        // loop trough all iterations from the simulations
        for (Integer simNr: allResults.keySet()) {
            // map for the score of each result in this simulationNr
            AbstractMap<String, Double> scoredSettings = new HashMap<>();

            // loop through all settings
            for (String key: allResults.get(simNr).keySet()) {
                // list for the ids in the result for this setting
                List<String> settingsList = new ArrayList<>();

                // loop through all pairs in single result
                for (General.Pair pair: allResults.get(simNr).get(key)) {
                    settingsList.add(pair.getKey());
                }
                // retrieve all linked documents
                List<List<String>> retrievedForSettings = NewRetriever.multiGetList(settingsList);

                // score the result list and its linked documents
                double score = RankBiasedClusters.runMeasureSingleResult(retrievedForSettings, p);

                // save the score with the setting as key
                scoredSettings.put(key, score);
            }
            // add the scores of the simulations to the main map
            scoredSimulations.put(simNr, scoredSettings);
        }

    }*/

    /*public static void compareWithRBD(Settings settings) throws Exception {
        // OBS!!
        double p = 0.7;
        System.out.println("OBS!!!! P is set withing method.");

        // read queries from file
        List<JsonObject> simulationsListOfJsons = FileReader.readJsonFromFile(settings.getSimulationPath());

        // parse jsons read from file
        AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allResults = MeasuresParser.parseListOfSimulationResults(simulationsListOfJsons);

        PrepClusterMetric.createListOfSets(allResults.get(0));
       *//*
        // list to store the results in
        AbstractMap<Integer, AbstractMap<String, Double>> scoresForAllIterations = compareAllCombinationsWithRBD(p, allResults);

        FileWriter.storeScoreInFileAsCsv(scoresForAllIterations, settings.getScorePath());*//*
    }

    private static AbstractMap<Integer, AbstractMap<String, Double>> compareAllCombinationsWithRBD
            (double p, AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allResults ) throws Exception {

        // map for returning all scores
        AbstractMap<Integer, AbstractMap<String, Double>> scoresForAllIterations = new HashMap<>();

        // map for temporary saving the results in loop
        AbstractMap<String, Double> scoresForIteration;

        // compare all lists from the same interation (the iterations are keys in the map)
        for (Integer itr : allResults.keySet()) {
            // compare the lists
            scoresForIteration = compareAllCases(allResults.get(itr), p);

            // save this iterations scores
            scoresForAllIterations.put(itr, scoresForIteration);
        }
        return scoresForAllIterations;
    }
*/

 /*   private static AbstractMap<String, Double>  compareAllCases(AbstractMap<String, List<General.Pair>> sims, double p) throws Exception {
        AbstractMap<String, Double> allMeasuredCases = new HashMap<>();
        // compare each set-up once with each other set-up
        for (String firstKey : sims.keySet()) {
            for (String secondKey : sims.keySet()) {
                // OBS makes sure that each comparison is only done once. (breaks when reaching the matrix diagonal)
                if (firstKey.equals(secondKey)){
                    break;
                }
                // compute the score
                double score = RankBiasedOverlap.computeRankBiasedDistance(sims.get(firstKey), sims.get(secondKey), p);

                // save the score in map using CSV format
                String name = firstKey + "," + secondKey;
                allMeasuredCases.put(name, score);
            }
        }
        return allMeasuredCases;
    }*/

}















