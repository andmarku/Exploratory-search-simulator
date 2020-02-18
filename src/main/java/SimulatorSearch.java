import java.util.ArrayList;
import java.util.List;

public class SimulatorSearch {
    static List<UtilityGeneral.Pair> produceRankedList(Settings settings, List<String> masterQuery, double expMultiplier,
                                                       int nrOfSubqueries) throws Exception {
        // segment the query
        List<List<String>> subQueries = SimulatorQueryCreator.segmentQuery(masterQuery, settings.getSizeOfFullQuery(), nrOfSubqueries);

        // create the ranked list
        List<UtilityGeneral.Pair> listedResults = SimulatorUtility.produceRankedListFromListOfQueries(
                subQueries, expMultiplier, settings.getSizeOfRetrievedList(), settings.getSizeOfFinalRankedList());

        return listedResults;
    }

}

