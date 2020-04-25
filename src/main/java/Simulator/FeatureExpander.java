package Simulator;

import java.util.*;

public class FeatureExpander {

    public static AbstractMap<String,Double> expandRanking(AbstractMap<String, Double> scoredDocs,
                                                           AbstractMap<String, Set<String>> v1,
                                                           AbstractMap<String, Set<String>> v2,
                                                           AbstractMap<String, Double> fcnParams){
/*
        System.out.println("Unchecked expansion component");
*/

        AbstractMap<String, Double> expandedRes = new HashMap<>();

        // sum each vicinity score and measure the vicinities sizes
        Map<String, Map<String,Double>> preppedDocs = prepForExpansion(scoredDocs, v1, v2);

        // rescore each document
        double newScore;
        for (String id: preppedDocs.keySet()) {
            newScore = 0;

            // evaluate function
            newScore = expansionScoringFunction(
                    preppedDocs.get(id).get("score"),
                    preppedDocs.get(id).get("v1Score"),
                    preppedDocs.get(id).get("v2Score"),
                    preppedDocs.get(id).get("v1SizeFraction"),
                    preppedDocs.get(id).get("v2SizeFraction"),
                    fcnParams.get("alpha"), fcnParams.get("beta"), fcnParams.get("eta"),
                    fcnParams.get("gamma1"), fcnParams.get("gamma2"));

            // add to final expanded scores
            expandedRes.put(id, newScore);
        }

        return expandedRes;
    }

    public static double expansionScoringFunction(double saScore, double v1Score, double v2Score,
                                                  double v1SizeFraction, double v2SizeFraction,
                                                  double alpha, double beta, double eta, double gamma1, double gamma2){
/*
        System.out.println("Unchecked exp function");
*/
        return  saScore * alpha +
                beta * ( gamma1 + (1-gamma1) * v1SizeFraction ) * v1Score +
                eta * ( gamma2 + (1-gamma2) * v2SizeFraction ) * v2Score;
    }

    public static Map<String, Map<String,Double>> prepForExpansion(AbstractMap<String, Double> scoredDocs,
                                                                   AbstractMap<String, Set<String>> v1,
                                                                   AbstractMap<String, Set<String>> v2){
/*
        System.out.println("Unchecked prep for expansion");
*/
        Map<String, Map<String,Double>> preppedDocs = new HashMap<>();

        double score;
        double v1Score;
        double v2Score;
        double v1SizeFraction;
        double v2SizeFraction;
        Map<String, Double> mapScores;
        for (String id : v1.keySet()) {
            // add the original score, if any
            score = 0;
            if (scoredDocs.containsKey(id)) {
                score += scoredDocs.get(id);
            }

            // sum all scores for v1 docs (if those docs are scored)
            v1Score = 0;
            for (String v1Id : v1.get(id)) {
                if (scoredDocs.containsKey(v1Id)) {
                    v1Score += scoredDocs.get(v1Id);
                }
            }

            // sum all scores for v2 docs (if those docs are scored)
            v2Score = 0;
            for (String v2Id : v2.get(id)) {
                if (scoredDocs.containsKey(v2Id)) {
                    v2Score += scoredDocs.get(v2Id);
                }
            }

            // store the normalisation constant as either 1/size or as 0. The latter if size = 0.
            if (v1.size() != 0){
                v1SizeFraction = 1 / (double) v1.size();
            }else{
                v1SizeFraction = 0;
            }
            if (v2.size() != 0){
                v2SizeFraction = 1 / (double) v2.size();
            }else{
                v2SizeFraction = 0;
            }

            mapScores = new HashMap<>();
            mapScores.put("score", score);
            mapScores.put("v1Score", v1Score);
            mapScores.put("v2Score", v2Score);
            mapScores.put("v1SizeFraction", v1SizeFraction);
            mapScores.put("v2SizeFraction", v2SizeFraction);
            preppedDocs.put(id, mapScores);
        }
        return preppedDocs;
    }
}
