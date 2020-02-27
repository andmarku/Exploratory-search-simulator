package ManualQueries;

import SearchEngine.Model;
import SearchEngine.SearchEngineWrapper;
import SearchEngine.SimpleModel;
import Settings.Settings;
import Simulator.QueryCreator;
import Utility.General;

import java.util.ArrayList;
import java.util.List;

class Trial {
    /*static List<List<General.Pair>> trialSimulator(Settings settings, List<String> masterQueries) throws Exception {
        // --- SET-UP ---
        // Choose model (currently only one)
        Model rankingModel = new SimpleModel();

        // general parameters
        int numOfItr = settings.getNumOfItr();
        int sizeOfFullQuery = settings.getSizeOfFullQuery();
        int sizeOfFinalRankedList = settings.getSizeOfFinalRankedList();
        int sizeOfRetrievedList = settings.getSizeOfRetrievedList();

        // specific to trial case
       *//* double expansionMultiplier = settings.getExpansionMultiplier();*//*
        int numOfSubQueries = settings.getNumOfSubQueries();

        // --- SIMULATION ---

        // list to store results in
        List<List<General.Pair>> simulatedTrialCases = new ArrayList<>();
        for (int i = 0; i < numOfItr; i++) {
            // pick out this iterations master query
            List<String> masterQuery = masterQueries.subList(i * sizeOfFullQuery, (i + 1) * sizeOfFullQuery);

            // segment the query
            List<List<String>> subQueries = QueryCreator.segmentQuery(masterQuery, sizeOfFullQuery, numOfSubQueries);

            // create result
            List<General.Pair> rankedListAsList = rankingModel.produceRankedList(settings, expMultiplier, searchResultLists);

            // storing ---
            simulatedTrialCases.add(listedResults_trial);

*//*            // some printing
            System.out.println("Master query of itr " + i + " is " + masterQuery);
            System.out.println("Subqueries of itr " + i + " are " + subQueries);
            // printing to the console for testing
            Utility.UtilityConsolePrinting.printMyRankedList("The case " + settings.simulationName, listedResults_trial);*//*
        }

        return simulatedTrialCases;
    }*/

}
