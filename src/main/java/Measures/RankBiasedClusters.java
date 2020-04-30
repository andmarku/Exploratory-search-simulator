package Measures;

import java.util.*;

public class RankBiasedClusters {

/*    public static double runMeasureSingleResult(List<List<String>> listOfLinks, double p){
        List<Set<String>> setsInResult = new ArrayList<>();

        // through all lists of linked document (incl original doc)
        for (List<String> listOfIds : listOfLinks) {
            Set mySet = new HashSet();

            // through all linked documents (incl original doc)
            for (String docId : listOfIds) {
                mySet.add(docId);
            }
            setsInResult.add(mySet);
        }

        // calculate the score for a single result list
        double score = computeRankBiasedClusters(setsInResult,p);

        return score;
    }
    */


    public static double computeRankBiasedClusters(List<Integer> listOfNrOfClusterPerDepth, double p){
        int nrOfClusters = 0;
        double score = 0;

        // start at second entry
        for (int indexOfDepth = 0; indexOfDepth < listOfNrOfClusterPerDepth.size(); indexOfDepth++) {

            // make index inclusive
            nrOfClusters = listOfNrOfClusterPerDepth.get(indexOfDepth);

            // depth to compute (actual depth for second article starts at 2, not 0, hence i + 1)
            int depth = indexOfDepth + 2;

            // compute score at depth k
            score += computeOverlapFormulaAtDepthK(nrOfClusters, depth, p);

        }
        // normalise (max sum from infinite loop is 1/(1 - p) )
        score = score * (1-p);

        return score;
    }

    private static double computeOverlapFormulaAtDepthK(int nrOfClusters, double k, double p){
        double ratio = (k - nrOfClusters)/k;
        return Math.pow(p, k-1) * ratio;
    }

}
