package SearchEngine;

import Settings.Settings;
import Utility.General;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class SimpleModel implements Model {
    public List<General.Pair> produceRankedList(Settings settings, double expMultiplier, List<AbstractMap<String, AbstractMap<String, Double>>> searchResultLists) throws Exception {
        // EXPANSION
        List<AbstractMap<String, Double>> storedExpandedLists = new ArrayList<>();
        for (AbstractMap<String, AbstractMap<String, Double>> searchResult : searchResultLists) {
            AbstractMap<String, Double> scoredDocs = searchResult.get("scoredDocs");
            AbstractMap<String, Double> linkedDocs = searchResult.get("linkedDocs");
            double totalScore = searchResult.get("totalScore").get("totalScore");

            // Rescore wtr to expansion
            AbstractMap<String, Double> expandedResults =
                    FeatureExpander.docAndPercentageLinkScorer(scoredDocs, linkedDocs, totalScore, expMultiplier);

            // store
            storedExpandedLists.add(expandedResults);
        }


        // COMBINATION of all sub-queries into a final list
        AbstractMap<String, Double> scoredResults = FeatureCombiner.multiplierCombiner(storedExpandedLists);

        // pick out the first x of the scored docs
        return General.listRankedResults(scoredResults, settings.getSizeOfFinalRankedList());
    }
}
