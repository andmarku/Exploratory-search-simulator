package Measures;

import Settings.Settings;
import Utility.FileReader;
import Utility.General;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class MeasureWrapper {
    public static List<Double> compareAllCombinationsWithRBD(Settings settings) throws Exception {
        // read queries from file
        List<JsonObject> simulationsListOfJsons = FileReader.readJsonFromFile(settings.getSimulationPath());
        /*List<List<General.Pair>> simulations =*/
        Parser.parseListOfSimulationResults(simulationsListOfJsons);

        // compare with statistical test
        //*return StatisticalRankComparision.compareAllCases(baseLines, mySims, settings.p);*//*
        return null;
    }

    /*
    public static List<Double> compareAllCases(List<List<General.Pair>> listOfOrderedListsOne,
                                               List<List<General.Pair>> listOfOrderedListsTwo, double p) throws Exception {

        List<Double> scores = new ArrayList<>();

        if(listOfOrderedListsTwo.size() != listOfOrderedListsOne.size()){
            throw new Exception("Illegal comparison: Different size of lists - wrong number of cases");
        }

        for (int i = 0; i < listOfOrderedListsOne.size(); i++) {
            scores.add(compareRankedLists(listOfOrderedListsOne.get(i), listOfOrderedListsTwo.get(i), p));
        }

        return scores;
    }*/

}
