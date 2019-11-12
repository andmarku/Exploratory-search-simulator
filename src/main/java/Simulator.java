import javax.json.JsonObject;
import java.io.IOException;
import java.util.*;

// TODO: 2019-10-30 general observation: the ranking score from elastic is not normalized. I need to do this myself when combining
class Simulator {


    static void mySimulator(SimulatorSettings settings) throws IOException {
        /*----------------------------*/
        /* --- GENERAL SET-UP --- */
        /*----------------------------*/
        // list to store results in
        List<JsonObject> allSimulationResults = new ArrayList<>();
        allSimulationResults.add(UtilityJsonCreator.createJsonObjectFromSettings(settings));
        // path to store result in
        String pathToFolder = settings.pathToFolder;
        String simulationName = settings.simulationName;
        // general parameters
        int numOfItr = settings.numOfItr;
        int sizeOfFullQuery = settings.sizeOfFullQuery;
        int sizeOfFinalRankedList = settings.sizeOfFinalRankedList;
        int sizeOfRetrievedList = settings.sizeOfRetrievedList;

        // trial case
        double expansionMultiplier = settings.expansionMultiplier;
        int numOfSubQueries = settings.numOfSubQueries;

        // create the master query
        List<String> masterQueries = SimulatorQueryCreator.createAllMasterQueries(numOfItr*sizeOfFullQuery, 100);
        System.out.println("All master queries are " + masterQueries);

        for (int i = 0; i < numOfItr; i++) {
            /*----------------------------*/
            /* --- SIMULATION: set-up --- */
            /*----------------------------*/
            List<String> masterQuery = masterQueries.subList(i*sizeOfFullQuery, (i + 1) * sizeOfFullQuery);
            System.out.println("Master query of itr " + i + " is " + masterQuery);

            /*----------------------------*/
            /* --- SIMULATION: base case --- */
            /*----------------------------*/
            List<List<String>> baseQuery = new ArrayList<>();
            baseQuery.add(masterQuery);
            // create the ranked list
            List<UtilityGeneral.Pair> listedResults_base =
                    SimulatorUtility.produceRankedListFromBaseQuery(baseQuery, sizeOfRetrievedList, sizeOfFinalRankedList);

            // printing to the console for testing
            //ConsolePrinting.printMyRankedList("The base case", listedResults_base);


            /*----------------------------*/
            /* --- SIMULATION: trial case --- */
            /*----------------------------*/
            // segment the query
            List<List<String>> subQueries = SimulatorQueryCreator.segmentQuery(masterQuery, sizeOfFullQuery, numOfSubQueries);
            // create the ranked list
            List<UtilityGeneral.Pair> listedResults_trial = SimulatorUtility.produceRankedListFromListOfQueries(
                    subQueries, expansionMultiplier, sizeOfRetrievedList, sizeOfFinalRankedList);

            // printing to the console for testing
            //ConsolePrinting.printMyRankedList("The trial case", listedResults_trial);


            /*----------------------------*/
            /* --- END of ITERATION: storing --- */
            /*----------------------------*/
            allSimulationResults.add(
                    UtilityJsonCreator.createJsonObjectFromTwoResults(
                            listedResults_base,
                            listedResults_trial));

            /*----------------------------*/
            /* --- END OF SIMULATION: storing --- */
            /*----------------------------*/
            // JsonPrinter.storeResultsInFile(allSimulationResults, pathToFolder, simulationName);
        }

    }

}// end of simulator class
