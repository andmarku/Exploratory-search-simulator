package Measures;

import java.util.*;

public class RankBiasedClusters {

    public static double computeBatchRankBiasedClusters(List<Integer> listOfNrOfClusterPerDepth, int batchSize, double p){
        double score = 0;

        // minus one since indices in arrays starts at zero
        for (int indexOfDepth = batchSize - 1; indexOfDepth < listOfNrOfClusterPerDepth.size(); indexOfDepth += batchSize) {

            // make index inclusive
            int nrOfClusters = listOfNrOfClusterPerDepth.get(indexOfDepth);

            // (index in sum starts at 1, not 0 which is the index in the array, hence i + 1)
            int indexInSum = indexOfDepth + 1;
            // (depth starts at 2, not 0 which is the index in the array, hence i + 1)
            int depth = indexOfDepth + 2;

            // compute score at depth k
            score += computeOverlapFormulaAtDepthK(nrOfClusters, depth, indexInSum, p);

        }
        // normalise (max sum from infinite loop is 1/(1 - p) )
        score = score * (1-p);

        return score;
    }

    private static double computeOverlapFormulaAtDepthK(int nrOfClusters, double depth, int k, double p){
        double ratio = (depth - nrOfClusters)/(depth-1);
        return Math.pow(p, k-1) * ratio;
    }

}
