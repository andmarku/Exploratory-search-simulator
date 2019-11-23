import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

class SimulatorBaseCase {

    static List<JsonObject>  simulateBaseCase(SimulatorSettings settings, List<String> masterQueries) throws Exception {
        List<JsonObject> allSimulationResults = new ArrayList<>();

        allSimulationResults.add(UtilityJsonCreator.createJsonObjectFromSettings(settings));

        int sizeOfRetrievedList = settings.sizeOfRetrievedList;
        int sizeOfFinalRankedList = settings.sizeOfFinalRankedList;
        int sizeOfFullQuery = settings.sizeOfFullQuery;

        for (int i = 0; i < settings.numOfItr; i++) {
         /*   ----------------------------
             --- SIMULATION: set-up ---
            ----------------------------*/
            List<String> masterQuery = masterQueries.subList(i * sizeOfFullQuery, (i + 1) * sizeOfFullQuery);
            System.out.println("Master query of itr " + i + " is " + masterQuery);

           /* ----------------------------
             --- SIMULATION: base case ---
            ----------------------------*/
            List<List<String>> baseQuery = new ArrayList<>();
            baseQuery.add(masterQuery);
            // create the ranked list
            List<UtilityGeneral.Pair> listedResults_base =
                    SimulatorUtility.produceRankedListFromBaseQuery(baseQuery, sizeOfRetrievedList, sizeOfFinalRankedList);

            /*----------------------------
             --- END of ITERATION: storing ---
            ----------------------------*/
            allSimulationResults.add(UtilityJsonCreator.createJsonObjectFromPairs(listedResults_base));

        }
        return allSimulationResults;
    }
}
