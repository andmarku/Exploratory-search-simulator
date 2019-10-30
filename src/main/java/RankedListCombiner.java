import java.util.*;

public class RankedListCombiner {

    /*
    * Adds the lists into a single list with each docs score being the sum of its individual scores
    * */
    public static AbstractMap<String, Double> sumQueryResultCombiner
            (List<AbstractMap<String, Double>> queries){
        if (queries.size() == 1){
            return queries.get(0);
        }

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
            newScore = combine(preliminaryRes.get(docId), 1);
            combinedRes.put(docId, newScore );
        }
        return combinedRes;
    } // end of additionQueryResultCombiner

    /*
     * Very simple multiplier combiner
     * */
    public static double combine(List<Double> scores, double multiplier){
        double totalScore = 0;
        for (double queryScore: scores) {
            totalScore += queryScore;
        }
        return totalScore*multiplier;

    }
}
