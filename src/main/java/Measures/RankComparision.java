package Measures;

import Utility.General;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RankComparision {
    public static List<Double> compareAllCases(List<List<General.Pair>> listOfOrderedListsOne,
                                               List<List<General.Pair>> listOfOrderedListsTwo, double p) throws Exception {

        List<Double> scores = new ArrayList<>();

        if(listOfOrderedListsTwo.size() != listOfOrderedListsOne.size()){
            throw new Exception("Illegal comparison: Different size of lists - wrong number of cases");
        }

        for (int i = 0; i < listOfOrderedListsOne.size(); i++) {
            scores.add(compareRankedLists(listOfOrderedListsOne.get(i), listOfOrderedListsTwo.get(i), p));
        }

        return scores;
    }

    private static double compareRankedLists(List<General.Pair> orderedListOne, List<General.Pair> orderedListTwo, double p){

        int length = Math.min(orderedListOne.size(), orderedListTwo.size());
        double totalScore = 0;
        Set<String> joinedSet = new HashSet<>();
        for (int i = 0; i < length; i++) {
            joinedSet.add(orderedListOne.get(i).getKey());
            joinedSet.add(orderedListTwo.get(i).getKey());


            // k = total depth, which starts at 1
            totalScore += computeOverlapFormulaAtDepthK(joinedSet.size(), i + 1, p);
        }
        totalScore = totalScore * (1-p);
        System.out.println(joinedSet.size());
        return totalScore;
    }

    private static double computeOverlapFormulaAtDepthK(int sizeOfJoinedSet, double k, double p){
        // supposed to be size of this intersection / depth
        // sizeOfJoinedSet is assumed to be between k and 2 k
        double overlap = (2*k - sizeOfJoinedSet)/k;
        return Math.pow(p, k-1) * overlap;
    }
}
