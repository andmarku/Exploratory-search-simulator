import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

class SimulatorTrialCase {
    static List<List<UtilityGeneral.Pair>> trialSimulator(SimulatorSettings settings, List<String> masterQueries) throws Exception {
        // --- SET-UP ---

        // general parameters
        int numOfItr = settings.numOfItr;
        int sizeOfFullQuery = settings.sizeOfFullQuery;
        int sizeOfFinalRankedList = settings.sizeOfFinalRankedList;
        int sizeOfRetrievedList = settings.sizeOfRetrievedList;

        // specific to trial case
        double expansionMultiplier = settings.expansionMultiplier;
        int numOfSubQueries = settings.numOfSubQueries;

        // --- SIMULATION ---

        // list to store results in
        List<List<UtilityGeneral.Pair>> simulatedTrialCases = new ArrayList<>();
        for (int i = 0; i < numOfItr; i++) {
            // pick out this iterations master query
            List<String> masterQuery = masterQueries.subList(i * sizeOfFullQuery, (i + 1) * sizeOfFullQuery);

            // segment the query
            List<List<String>> subQueries = SimulatorQueryCreator.segmentQuery(masterQuery, sizeOfFullQuery, numOfSubQueries);

            // create the ranked list
            List<UtilityGeneral.Pair> listedResults_trial = SimulatorUtility.produceRankedListFromListOfQueries(
                    subQueries, expansionMultiplier, sizeOfRetrievedList, sizeOfFinalRankedList);

            // storing ---
            simulatedTrialCases.add(listedResults_trial);

/*            // some printing
            System.out.println("Master query of itr " + i + " is " + masterQuery);
            System.out.println("Subqueries of itr " + i + " are " + subQueries);
            // printing to the console for testing
            UtilityConsolePrinting.printMyRankedList("The case " + settings.simulationName, listedResults_trial);*/
        }

        return simulatedTrialCases;
    }

}
