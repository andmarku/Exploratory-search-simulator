package Measures;

import java.util.*;

public class RankBiasedClusters {

    public static double runMeasureSingleResult(List<List<String>> listOfLinks, double p){
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

    private static double computeRankBiasedClusters(List<Set<String>> orderedList, double p){
        int nrOfClusters = 0;
        double score = 0;

        // start at second entry
        for (int d = 1; d < orderedList.size(); d++) {
            // make index inclusive
            nrOfClusters = greedyAlgorithmFindingNumberOfClustersInList(orderedList.subList(0, d + 1));

            // compute score at depth k (actual depth for second article starts at 2, not 1, hence i + 1)
            score += computeOverlapFormulaAtDepthK(nrOfClusters, d+1, p);

        }
        // normalise (max sum from infinite loop is 1/(1 - p) )
        score = score * (1-p);

        return score;
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

                    // if set i is already added to a set, make sure not to check it against further sets
                    if (sweepAgain){
                        break;
                    }
                } // end loop through all sets higher on the list than set i
            }
        } // end while

        return sets.size() - indexesOfSetsToRemove.size();
    }

    private static double computeOverlapFormulaAtDepthK(int nrOfClusters, double k, double p){
        double ratio = (k - nrOfClusters)/k;
        return Math.pow(p, k-1) * ratio;
    }

}


       /* List<Set<String>> l = new ArrayList<>();

        Set<String> s = new TreeSet<>();
        s.add(1);
        s.add(2);
        s.add(3);
        s.add(4);
        s.add(5);
        l.add(s);

        s = new TreeSet<>();
        s.add(6);
        s.add(7);
        s.add(8);
        s.add(9);
        s.add(10);
        l.add(s);

        s = new TreeSet<>();
        s.add(1);
        s.add(11);
        s.add(12);
        s.add(13);
        s.add(14);
        l.add(s);

        s = new TreeSet<>();
        s.add(12);
        s.add(15);
        s.add(16);
        s.add(17);
        s.add(18);
        l.add(s);
        l.add(s);
        l.add(s);
        l.add(s);
*/