import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FeatureCombiner {

    static AbstractMap<String, Double> multiplierCombiner(List<AbstractMap<String, Double>> queries){
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

        for (AbstractMap<String, Double> query : queries) {
            for (String docId: query.keySet()) {
                if (allScoresForEachDoc.containsKey(docId)){
                    existingScoreList = allScoresForEachDoc.get(docId);
                    existingScoreList.add(query.get(docId));
                    allScoresForEachDoc.put(docId, existingScoreList);
                }else{
                    List<Double> newScoreList = new ArrayList<Double>();
                    newScoreList.add(query.get(docId));
                    // just add the document with its score if it had no match in the other query
                    allScoresForEachDoc.put(docId, newScoreList);
                }
            }
        }
        return allScoresForEachDoc;
    }

}
