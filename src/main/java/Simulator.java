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
        allSimulationResults.add(JsonCreator.createJsonObjectFromSettings(settings));
        // path to store result in
        String pathToFolder = settings.pathToFolder;
        String simulationName = settings.simulationName;
        // general parameters
        int numOfItr = settings.numOfItr;
        int sizeOfFullQuery = settings.sizeOfFullQuery;
        int sizeOfFinalRankedList = settings.sizeOfFinalRankedList;
        int sizeOfRetrievedList = settings.sizeOfRetrievedList;
        // base case
        double baseCaseExpansionMultiplier = settings.baseCaseExpansionMultiplier;
        int baseCaseNumOfSubQueries = settings.baseCaseNumOfSubQueries;
        // trial case
        double expansionMultiplier = settings.expansionMultiplier;
        int numOfSubQueries = settings.numOfSubQueries;


        // TODO: 2019-11-11  move master queries outside loop and just retrieve a longer list. from there
        // i can pick the x * numOfItr first documents instead of making numOfItr extra calls.


        for (int i = 0; i < numOfItr; i++) {

            /*----------------------------*/
            /* --- SIMULATION: set-up --- */
            /*----------------------------*/
            // create the master query
            List<String> masterQuery = QueryCreator.createMasterQuery(sizeOfFullQuery, 100);
            System.out.println(masterQuery);
            /*----------------------------*/
            /* --- SIMULATION: base case --- */
            /*----------------------------*/
            List<UtilityFunctions.Pair> listedRankedResults_base = SimulatorUtility.produceRankedList(
                    masterQuery, baseCaseExpansionMultiplier, baseCaseNumOfSubQueries,
                    sizeOfFullQuery, sizeOfRetrievedList, sizeOfFinalRankedList);

            // printing to the console for testing
            //ConsolePrinting.printMyRankedList("The base case", listedRankedResults_base);


            /*----------------------------*/
            /* --- SIMULATION: trial case --- */
            /*----------------------------*/
            List<UtilityFunctions.Pair> listedRankedResults_trial = SimulatorUtility.produceRankedList(
                    masterQuery, expansionMultiplier, numOfSubQueries, sizeOfFullQuery,
                    sizeOfRetrievedList, sizeOfFinalRankedList);

            // printing to the console for testing
            //ConsolePrinting.printMyRankedList("The trial case", listedRankedResults_trial);


            /*----------------------------*/
            /* --- END of ITERATION: storing --- */
            /*----------------------------*/
            allSimulationResults.add(
                    JsonCreator.createJsonObjectFromTwoResults(
                            listedRankedResults_base,
                            listedRankedResults_trial));

            /*----------------------------*/
            /* --- END OF SIMULATION: storing --- */
            /*----------------------------*/
            // JsonPrinter.storeResultsInFile(allSimulationResults, pathToFolder, simulationName);
        }

    }

}// end of simulator class
