package SearchEngine;

import java.util.*;

public class FeatureCombiner {

    public static AbstractMap<String, Double> multiplierCombiner(List<AbstractMap<String, Double>> queries){
        // test to make sure that there actually are multiple queries
        if (queries.size() == 1){
            return queries.get(0);
        }
        // get all scores in single list
        AbstractMap<String, List<Double>> allScoresForEachDoc = createListOfListWithAllScoresForEachDoc(queries);
        // actual multiplier combiner
        return combineScoresThroughMultiplying(allScoresForEachDoc);
    }

    private static AbstractMap<String, Double> combineScoresThroughMultiplying
            (AbstractMap<String, List<Double>> allScoresForEachDoc){

        AbstractMap<String, Double> finalScore = new HashMap<>();
        double newScore;
        for (String docId: allScoresForEachDoc.keySet()) {
            newScore = 1;
            for (double specificScore: allScoresForEachDoc.get(docId)) {
                newScore = newScore * specificScore;
            }
            finalScore.put(docId, newScore );
        }
        return finalScore;
    }

    private static AbstractMap<String, List<Double>> createListOfListWithAllScoresForEachDoc(List<AbstractMap<String, Double>> queries){
        AbstractMap<String, List<Double>> allScoresForEachDoc = new HashMap<>();
        List<Double> existingScoreList;

        double maxScoreForQuery;
        double queryScore;
        for (AbstractMap<String, Double> query : queries) {
            maxScoreForQuery = findMaxScore(query);
            for (String docId: query.keySet()) {
                queryScore = query.get(docId)/maxScoreForQuery;
                if (allScoresForEachDoc.containsKey(docId)){
                    existingScoreList = allScoresForEachDoc.get(docId);
                    existingScoreList.add(queryScore);
                    allScoresForEachDoc.put(docId, existingScoreList);
                }else{
                    List<Double> newScoreList = new ArrayList<Double>();
                    newScoreList.add(queryScore);
                    // just add the document with its score if it had no match in the other query
                    allScoresForEachDoc.put(docId, newScoreList);
                }
            }
        }
        return allScoresForEachDoc;
    }

    private static double findMaxScore (AbstractMap<String, Double> query){
        double maxScore = 0;
        for (double docScore: query.values()){
            if(docScore > maxScore){
                maxScore = docScore;
            }
        }
        return maxScore == 0? 1 : maxScore;
    }
}
