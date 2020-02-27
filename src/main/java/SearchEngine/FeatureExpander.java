package SearchEngine;

import java.util.AbstractMap;

public class FeatureExpander {
    /* idea: each linked doc gets sum of the percentage of the total score that the original documents had */
    public static AbstractMap<String,Double> docAndPercentageLinkScorer
    (AbstractMap<String, Double> scoredDocs, AbstractMap<String, Double> linkedDocs,
     double totalScore, double linkedScoreMultiplier){

        for (String docId : linkedDocs.keySet()) {
            Double linkedScore = linkedDocs.get(docId) * linkedScoreMultiplier;

            if (scoredDocs.containsKey(docId)){
                Double oldScore = scoredDocs.get(docId);
                scoredDocs.replace(docId, oldScore + linkedScore);
            }else{
                scoredDocs.put(docId, linkedScore);
            }
        }
        return scoredDocs;
    }
}
