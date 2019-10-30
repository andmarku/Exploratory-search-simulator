import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

// TODO: 2019-10-25 create a more clever bonus system
/*
problem - cant access stuff such as the value of linking docs unless the scoring is done in the parser
    Chosen solution: opt 2
    possible solutions
        1: send scorer as object into parser
            then i dont know what the total score was, since this is computed in the parser
        2: save links in hashmap instead with sum of points from orig docs as value
            then I can't change it easily afterwards
            then I can't do anything that uses which document gave what score
        3: save various stuff in a link object, which is stored in a hashmap
*/
public class QueryExpander {

    /*
     * Works together with docAndLinksScoreParser
     * idea: each linked doc gets sum of the percentage of the total score that the original documents had
     * */
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
    }// end of percentageScorer


    /*
     * Works together with docScoreAndJustLinksParser
     * */
    public static AbstractMap<String, Double> simpleDocAndLinkScorer
    (AbstractMap<String, Double> scoredDocs, List<String> linkedDocs, double totalScore){
        double bonusScore = linkedDocs.size() == 0? 0 : totalScore/linkedDocs.size(); // make sure not to divide by zero
        for (String docId : linkedDocs) {
            if (scoredDocs.containsKey(docId)){
                Double oldScore = scoredDocs.get(docId);
                scoredDocs.replace(docId, oldScore + bonusScore);
            }else{
                scoredDocs.put(docId, bonusScore);
            }
        }
        return scoredDocs;
    }// end of simpleDocAndLinkScorer

    /*
     * First attempt. Probably doesn't work anymore
     * */
    public static AbstractMap<String, Double> simpleScorer(List<String> retrievedDocs, List<String> linkedDocs){
        AbstractMap<String,Double> scoredDocs = new HashMap<String, Double>();

        double retrievedScore = retrievedDocs.size();
        for (String doc_id: retrievedDocs) {
            //OBS assumes that there are no duplicates, if there are the larger score will be overwritten for the id.
            scoredDocs.put(doc_id, retrievedScore);
            retrievedScore--;
        }

        double linkedScore = 0;
        for (String docId : linkedDocs) {
            if (scoredDocs.containsKey(docId)){
                double oldScore = scoredDocs.get(docId);
                scoredDocs.replace(docId, oldScore + linkedScore);
            }else{
                scoredDocs.put(docId, linkedScore);
            }
        }
        return scoredDocs;
    }//end of simpleScorer
}//end of class
