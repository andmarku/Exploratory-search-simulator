package Simulator;

import java.util.*;

public class FeatureExpander {

    public static AbstractMap<String,Double> expandRanking(Map<String, Map<String,Double>> preppedDocs,
                                                           List<Double> fcnParams){

        AbstractMap<String, Double> expandedRes = new HashMap<>();

        // rescore each document
        for (String id: preppedDocs.keySet()) {
            // evaluate function
            double newScore = expansionScoringFunction(
                    preppedDocs.get(id).get("score"),
                    preppedDocs.get(id).get("v1Score"),
                    preppedDocs.get(id).get("v1SizeFraction"),
                    fcnParams.get(0), fcnParams.get(1), fcnParams.get(2));

            // add to final expanded scores
            expandedRes.put(id, newScore);
        }
        return expandedRes;
    }

    public static double expansionScoringFunction(double v0Score, double v1Score, double v1SizeFraction,
                                                  double alpha, double beta, double gamma1){
        return  alpha  * v0Score +
                beta   * v1Score +
                gamma1 * v1SizeFraction * v1Score;
    }

    public static Map<String, Map<String,Double>> prepForExpansion(AbstractMap<String, Double> scoredDocs,
                                                                   AbstractMap<String, Set<String>> v1){

        Map<String, Map<String,Double>> preppedDocs = new HashMap<>();

        double v1SizeFraction;
        Map<String, Double> mapScores;
        for (String id : v1.keySet()) {
            // add the original score, if any
            double score = 0;
            if (scoredDocs.containsKey(id)) {
                score += scoredDocs.get(id);
            }

            // sum all scores for v1 docs (if those docs are scored)
            double v1Score = 0;
            for (String v1Id : v1.get(id)) {
                if (scoredDocs.containsKey(v1Id)) {
                    v1Score += scoredDocs.get(v1Id);
                }
            }

            // store the normalisation fraction constant as either 1/size or as 0. The latter if size = 0. (should never happen)
            if (v1.get(id).size() != 0){
                v1SizeFraction = 1 / (double) v1.get(id).size();
            }else{
                v1SizeFraction = 0;
            }

            mapScores = new HashMap<>();
            mapScores.put("score", score);
            mapScores.put("v1Score", v1Score);
            mapScores.put("v1SizeFraction", v1SizeFraction);
            preppedDocs.put(id, mapScores);
        }

        return preppedDocs;
    }
}
