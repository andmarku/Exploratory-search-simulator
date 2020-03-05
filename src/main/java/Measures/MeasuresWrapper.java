package Measures;

import Settings.Settings;
import Utility.FileReader;
import Utility.FileWriter;
import Utility.General;

import javax.json.JsonObject;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeasuresWrapper {
    public static void compareWithRBD(Settings settings) throws Exception {
        // OBS!!
        double p = 0.7;
        System.out.println("OBS!!!! P is (temporary) set withing method.");

        // read queries from file
        List<JsonObject> simulationsListOfJsons = FileReader.readJsonFromFile(settings.getSimulationPath());

        // parse jsons read from file
        AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allResults = MeasuresParser.parseListOfSimulationResults(simulationsListOfJsons);

        PrepClusterMetric.createListOfSets(allResults.get(0));
       /*
        // list to store the results in
        AbstractMap<Integer, AbstractMap<String, Double>> scoresForAllIterations = compareAllCombinationsWithRBD(p, allResults);

        FileWriter.storeScoreInFileAsCsv(scoresForAllIterations, settings.getScorePath());*/
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


    private static AbstractMap<String, Double>  compareAllCases(AbstractMap<String, List<General.Pair>> sims, double p) throws Exception {
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
    }

}















