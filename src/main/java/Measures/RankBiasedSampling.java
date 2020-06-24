package Measures;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankBiasedSampling {

    public static double computeBatchRankBiasedSampling(List<Integer> nrOfClustersAtEachDepthStartingAtDepth2, int batchSize,
                                                   double outerP, double innerP){
        double outerScore = 0;
        // minus one since indices in arrays starts at zero
        for (int d = batchSize-1; d < nrOfClustersAtEachDepthStartingAtDepth2.size(); d+=batchSize) {
            double indexOfEntryInSum;
            double actualDepth;
            double innerScore = 0;

            // minus one since indices in arrays starts at zero
            for (int j = batchSize-1; j <= d; j+=batchSize) {
                // since when j = 0, the nrOfClusters = 2
                actualDepth = (double) j + 2;
                double a_j = nrOfClustersAtEachDepthStartingAtDepth2.get(j) / actualDepth;

                // since j = 0 is the first entry in the sum
                indexOfEntryInSum = (double) j + 1;
                innerScore += a_j * calcW(indexOfEntryInSum, innerP);
            }

            // since when d = 0, the nrOfClusters = 2
            actualDepth = (double) d + 2;
            double a_d = (1 - nrOfClustersAtEachDepthStartingAtDepth2.get(d) / actualDepth) * innerScore;

            // since d = 0 is the first entry in the sum
            indexOfEntryInSum = (double) d + 1;
            outerScore += a_d * calcW(indexOfEntryInSum, outerP);
        }

        return outerScore;
    }

    private static double calcW(double depth, double p){
        return (1-p) * Math.pow(p, depth-1);
    }

}
