package Measures;

import Utility.General;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RankBiasedOverlap {

    /*
    Computes the metric Ranked Biased Distance
     */
    public static double computeRankBiasedDistance(List<General.Pair> orderedListOne, List<General.Pair> orderedListTwo, double p){
        double rBO = computeRboMin(orderedListOne, orderedListTwo, p);

        // the measure is defined as 1 - totalScore (since higher total score means more similar lists)
        return 1 - rBO;
    }

    /*
    Computes the lower bound on the metric Ranked Biased Overlap
    (plenty of code overlap with method computeRankBiasedOverlap. This was done since results used along the way are needed
    in the last step.)
    (also plenty of opportunities for improving performance here)
     */
    private static double computeRboMin(List<General.Pair> orderedListOne, List<General.Pair> orderedListTwo, double p){
        // length shared by both list (cannot compare further down)
        int sizeOfOverlap = Math.min(orderedListOne.size(), orderedListTwo.size());

        // set containing all items in both list down to depth k
        Set<String> joinedSet = new HashSet<>();

        double score = 0;

        // compute the sum in the formula
        double overlapAtK = 0;
        for (int i = 0; i < sizeOfOverlap; i++) {
            // add both items at current depth (items are only added if not already present in set)
            joinedSet.add(orderedListOne.get(i).getKey());
            joinedSet.add(orderedListTwo.get(i).getKey());

            // compute score at depth k (actual depth starts at 1, not 0, hence i + 1)
            overlapAtK = computeOverlapFormulaAtDepthK(joinedSet.size(), i + 1, p);
            score += overlapAtK;
        }

        // normalise (max sum from infinite loop is 1/(1 - p) )
        score = score * (1-p);

        // term to use in extrapolation
        double infSum = overlapAtK * Math.log(1-p) / p;

        // term to use in extrapolation
        // (overlapAtK * (sum of p^(d-1)/d from d = 1 to sizeOfOverlap)
        double partialSum = 0;
        for (int i = 1; i < sizeOfOverlap + 1; i++) {
            partialSum += Math.pow(p, i-1);
        }
        partialSum = partialSum * overlapAtK;

        // extrapolate
        score = score - infSum - partialSum;

        return score;
    }

    /*
    Computes the basic version of the metric Ranked Biased Overlap up to max overlap
     */
    private static double computeRankBiasedOverlap(List<General.Pair> orderedListOne, List<General.Pair> orderedListTwo, double p){
        // length shared by both list (cannot compare further down)
        int sizeOfOverlap = Math.min(orderedListOne.size(), orderedListTwo.size());

        // set containing all items in both list down to depth k
        Set<String> joinedSet = new HashSet<>();

        double score = 0;

        // compute the sum in the formula
        for (int i = 0; i < sizeOfOverlap; i++) {
            // add both items at current depth (items are only added if not already present in set)
            joinedSet.add(orderedListOne.get(i).getKey());
            joinedSet.add(orderedListTwo.get(i).getKey());

            // compute score at depth k (actual depth starts at 1, not 0, hence i + 1)
            score += computeOverlapFormulaAtDepthK(joinedSet.size(), i + 1, p);
        }

        // normalise (max sum from infinite loop is 1/(1 - p) )
        score = score * (1-p);

        return score;
    }

    /*
    The method calculates the score at depth k, defined as (size of overlap / depth) * p^(k-1).
    OBS! Overlap is NOT equal to size of joined set.
     */
    private static double computeOverlapFormulaAtDepthK(int sizeOfJoinedSet, double k, double p){
        // The size of the joined set of two lists at depth must be between k and 2 k, where size k means full overlap
        // and 2k means no overlap.
        double overlap = (2*k - sizeOfJoinedSet)/k;
        return Math.pow(p, k-1) * overlap;
    }
}
