package SearchEngine;

import Settings.Settings;
import Simulator.QueryCreator;
import Utility.General;

import java.util.List;

public class SearchEngine {
    public static List<General.Pair> produceRankedList(Settings settings, List<String> masterQuery, double expMultiplier,
                                                       int nrOfSubqueries) throws Exception {
        // segment the query
        List<List<String>> subQueries = QueryCreator.segmentQuery(masterQuery, settings.getSizeOfFullQuery(), nrOfSubqueries);

        // create the ranked list
        List<General.Pair> listedResults = Utility.produceRankedListFromListOfQueries(
                subQueries, expMultiplier, settings.getSizeOfRetrievedList(), settings.getSizeOfFinalRankedList());

        return listedResults;
    }

}

