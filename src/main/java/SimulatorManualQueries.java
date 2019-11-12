import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SimulatorManualQueries {

    static void runManualQueries(List<List<String>> subqueries, SimulatorSettings settings) throws IOException {
        /*----------------------------*/
        /* --- GENERAL SET-UP --- */
        /*----------------------------*/
        // general parameters
        int sizeOfFinalRankedList = settings.sizeOfFinalRankedList;
        int sizeOfRetrievedList = settings.sizeOfRetrievedList;
        // trial case
        double expansionMultiplier = settings.expansionMultiplier;


        // create the base query
        List<List<String>> baseQuery = new ArrayList<>();
        List<String> masterQuery = new ArrayList<>();
        for (List<String> query :subqueries) {
            masterQuery.addAll(query);
        }
        baseQuery.add(masterQuery);

        /*----------------------------*/
        /* --- SIMULATION: base case --- */
        /*----------------------------*/
        System.out.println("The base case query is " + masterQuery);
        List<UtilityGeneral.Pair> listedResults_base =
                SimulatorUtility.produceRankedListFromBaseQuery(baseQuery, sizeOfRetrievedList, sizeOfFinalRankedList);
        // printing to the console
        UtilityConsolePrinting.printMyRankedList("The base case", listedResults_base);


        /*----------------------------*/
        /* --- SIMULATION: trial case --- */
        /*----------------------------*/
        System.out.println("The trial case queries are " + subqueries);
        List<UtilityGeneral.Pair> listedRankedResults_trial = SimulatorUtility.produceRankedListFromListOfQueries(
                subqueries, expansionMultiplier, sizeOfRetrievedList, sizeOfFinalRankedList);
        // printing to the console
        UtilityConsolePrinting.printMyRankedList("The trial case", listedRankedResults_trial);
    }
}
