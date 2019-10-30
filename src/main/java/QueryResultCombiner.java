import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryResultCombiner {

    public static AbstractMap<String, Double> queryResultCombiner
            (List<AbstractMap<String, Double>> queries,
             ScoreCombiner scoreCombiner){

        AbstractMap<String, Double> combinedRes = new HashMap<>();
        AbstractMap<String, List<Double>> preliminaryRes = new HashMap<>();

        List<Double> existingScoreList;
        for (AbstractMap<String, Double> query : queries) {
            for (String docId: query.keySet()) {
                if (preliminaryRes.containsKey(docId)){
                    existingScoreList = preliminaryRes.get(docId);
                    existingScoreList.add(query.get(docId));
                    preliminaryRes.put(docId, existingScoreList);
                }else{
                    List<Double> newScoreList = new ArrayList<Double>();
                    newScoreList.add(query.get(docId));
                    // just add the document with its score if it had no match in the other query
                    preliminaryRes.put(docId, newScoreList);
                }
            }
        }

        double newScore;
        for (String docId: preliminaryRes.keySet()) {
            newScore = scoreCombiner.combine(preliminaryRes.get(docId));
            combinedRes.put(docId, newScore );
        }
        return combinedRes;
    } // end of queryResultCombiner
}// end of class
