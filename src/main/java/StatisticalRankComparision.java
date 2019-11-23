import java.util.ArrayList;
import java.util.List;

class StatisticalRankComparision {
    static List<Double> compareAllCases(List<List<UtilityGeneral.Pair>> listOfOrderedListsOne, List<List<UtilityGeneral.Pair>> listOfOrderedListsTwo) throws Exception {
        List<Double> scores = new ArrayList<>();

        if(listOfOrderedListsTwo.size() != listOfOrderedListsOne.size()){
            throw new Exception("Illegal comparison: Different size of lists - wrong number of cases");
        }

        for (int i = 0; i < listOfOrderedListsOne.size(); i++) {
            scores.add(compareRankedLists(listOfOrderedListsOne.get(i), listOfOrderedListsTwo.get(i)));
        }

        return scores;
    }

    private static double compareRankedLists(List<UtilityGeneral.Pair> orderedListOne, List<UtilityGeneral.Pair> orderedListTwo){
        return 1;
    }
}
