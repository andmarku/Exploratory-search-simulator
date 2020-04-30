package Measures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RankBiasedSampling {
/*    public static double runMeasureSingleResult(List<List<String>> listOfLinks, double outerP, double innerP){
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
        double score = computeRankBiasedSampling(setsInResult, outerP, innerP);

        return score;
    }*/

    public static double computeRankBiasedSampling(List<Integer> nrOfClustersAtEachDepthStartingAtDepth2,
                                                   double outerP, double innerP){
        double outerScore = 0;
        for (int d = 0; d < nrOfClustersAtEachDepthStartingAtDepth2.size(); d++) {
            double indexOfEntryInSum;
            double actualDepth;
            double innerScore = 0;

            // does not include d = j = 0
            for (int j = 0; j < d; j++) {
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


    private static int greedyAlgorithmFindingNumberOfClustersInList(List<Set<String>> sets) {

        // avoid unnecessary work without tampering with the list sets during iteration
        List<Integer> indexesOfSetsToRemove = new ArrayList<>();

        boolean sweepAgain = true;
        while (sweepAgain) {
            sweepAgain = false;

            // through all sets
            for (int i = 0; i < sets.size(); i++) {
                // avoid unnecessary work
                if (indexesOfSetsToRemove.contains(i)){
                    continue;
                }

                // through all sets higher on the list
                for (int j = 0; j < i; j++) {
                    // avoid unnecessary work
                    if (indexesOfSetsToRemove.contains(j)){
                        continue;
                    }

                    //through all connections in set i
                    for (String id: sets.get(i)) {
                        // if set i has a connection to set j, add set i to set j, and add set i to sets to remove list
                        if(sets.get(j).contains(id)){
                            sets.get(j).addAll(sets.get(i));
                            indexesOfSetsToRemove.add(i);
                             sweepAgain = true;
                            break;
                        }

                    } // end loop through all connections for set i

                    // if set i is already added a set, make sure not to check it against further sets
                    if (sweepAgain){
                        break;
                    }
                } // end loop through all sets higher on the list than set i
            }
        } // end while

        return sets.size() - indexesOfSetsToRemove.size();
    }
}
