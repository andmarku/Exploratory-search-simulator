package SearchEngine;

import Settings.Settings;
import Utility.General;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class SimpleModel implements Model {
    public List<General.Pair> produceExpandedRankedList(Settings settings, double expMultiplier, AbstractMap<String, AbstractMap<String, Double>> searchResult) {
        AbstractMap<String, Double> scoredDocs = searchResult.get("scoredDocs");
        AbstractMap<String, Double> linkedDocs = searchResult.get("linkedDocs");
        double totalScore = searchResult.get("totalScore").get("totalScore");

        // Rescore wtr to expansion
        AbstractMap<String, Double> expandedResults =
                FeatureExpander.docAndPercentageLinkScorer(scoredDocs, linkedDocs, totalScore, expMultiplier);

        // pick out the first x of the scored docs
        return General.listRankedResults(expandedResults, settings.getSizeOfFinalRankedList());
    }
}
