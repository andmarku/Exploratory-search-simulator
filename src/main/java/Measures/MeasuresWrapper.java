package Measures;

import Settings.Settings;
import Utility.FileReader;
import Utility.General;

import javax.json.JsonObject;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeasuresWrapper {
    public static List<Double> compareAllCombinationsWithRBD(Settings settings) throws Exception {
        // read queries from file
        List<JsonObject> simulationsListOfJsons = FileReader.readJsonFromFile(settings.getSimulationPath());

        // parse jsons read from file
        AbstractMap<Integer, AbstractMap<String, List<General.Pair>>> allResults = MeasuresParser.parseListOfSimulationResults(simulationsListOfJsons);
        System.out.println("Size of allresults is " + allResults.size());

        AbstractMap<String, Double> scoresForIteration;
        double p = 0.7;
        AbstractMap<String, List<General.Pair>> simsForItr;

        for (Integer itr : allResults.keySet()) {
            simsForItr = allResults.get(itr);

            scoresForIteration = compareAllCases(simsForItr, p);
            System.out.println("------ This iteration is " + itr + " " + scoresForIteration.size());
            for (String key: scoresForIteration.keySet()) {
                System.out.println(key + " score " + scoresForIteration.get(key));
            }
        }
        // THEN START WITH WRITER WHICH SAVES RBD AND WHICH LIST THAT HAVE BEEN COMPARED

        // compare with statistical test
        //*return StatisticalRankComparision.compareAllCases(baseLines, mySims, settings.p);*//*
        return null;
    }


    public static AbstractMap<String, Double>  compareAllCases(AbstractMap<String, List<General.Pair>> sims, double p) throws Exception {
        AbstractMap<String, Double> allMeasuredCases = new HashMap<>();
        for (String firstKey : sims.keySet()) {
            for (String secondKey : sims.keySet()) {
                // OBS makes sure that each comparison is only done once.
                if (firstKey.equals(secondKey)){
                    break;
                }
                double score = RankBiasedOverlap.computeRankBiasedDistance(sims.get(firstKey), sims.get(secondKey), p);
                String name = firstKey + "AND" + secondKey;
                allMeasuredCases.put(name, score);
            }
        }
        return allMeasuredCases;
    }

}















