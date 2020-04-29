package Simulator;

import java.util.*;

public class FeatureExpanderWithV2 {
    public static AbstractMap<String,Double> expandRanking(AbstractMap<String, Double> scoredDocs,
                                                           AbstractMap<String, Set<String>> v1,
                                                           AbstractMap<String, Set<String>> v2,
                                                           List<Double> fcnParams){

        AbstractMap<String, Double> expandedRes = new HashMap<>();

        // sum each vicinity score and measure the vicinities sizes
        Map<String, Map<String,Double>> preppedDocs = prepForExpansion(scoredDocs, v1, v2);

        // rescore each document
        for (String id: preppedDocs.keySet()) {
            // evaluate function
            double newScore = expansionScoringFunction(
                    preppedDocs.get(id).get("score"),
                    preppedDocs.get(id).get("v1Score"),
                    preppedDocs.get(id).get("v2Score"),
                    preppedDocs.get(id).get("v1SizeFraction"),
                    preppedDocs.get(id).get("v2SizeFraction"),
                    fcnParams.get(0), fcnParams.get(1), fcnParams.get(2),
                    fcnParams.get(3), fcnParams.get(4));

            // add to final expanded scores
            expandedRes.put(id, newScore);
        }

        return expandedRes;
    }

    public static double expansionScoringFunction(double s_aScore, double v1Score, double v2Score,
                                                  double v1SizeFraction, double v2SizeFraction,
                                                  double alpha, double beta, double eta, double gamma1, double gamma2){
        return  s_aScore * alpha +
                beta * ( gamma1 + (1-gamma1) * v1SizeFraction ) * v1Score +
                eta * ( gamma2 + (1-gamma2) * v2SizeFraction ) * v2Score;
    }

    public static Map<String, Map<String,Double>> prepForExpansion(AbstractMap<String, Double> scoredDocs,
                                                                   AbstractMap<String, Set<String>> v1,
                                                                   AbstractMap<String, Set<String>> v2){

        Map<String, Map<String,Double>> preppedDocs = new HashMap<>();

        double v1SizeFraction;
        double v2SizeFraction;
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

            // sum all scores for v2 docs (if those docs are scored)
            double v2Score = 0;
            for (String v2Id : v2.get(id)) {
                if (scoredDocs.containsKey(v2Id)) {
                    v2Score += scoredDocs.get(v2Id);
                }
            }


            if(score != v1Score){
                if(v2Score != v1Score){
                    System.out.println("All three scores are NOT the same");
                }else{
                    System.out.println("Some scores are the same");
                }
            }/*else {
            if (v2Score == score) {
                System.out.println("Same score in s_A and v2");
            }
            if (score == v1Score) {
                System.out.println("Same score in s_A and v1");
            }
            if (v2Score == v1Score) {
                System.out.println("Same score in v1 and v2");
            }
        }
*/
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
