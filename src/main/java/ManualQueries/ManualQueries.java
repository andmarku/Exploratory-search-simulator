package ManualQueries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManualQueries {

    /*static void runManualQueries(List<List<String>> subqueries, Settings.Settings settings) throws Exception {
        *//*----------------------------*//*
        *//* --- GENERAL SET-UP --- *//*
        *//*----------------------------*//*
        // general parameters
        int sizeOfFinalRankedList = settings.getSizeOfFinalRankedList();
        int sizeOfRetrievedList = settings.getSizeOfRetrievedList();
        // trial case
        double expansionMultiplier = settings.getExpansionMultiplier();


        // create the base query
        List<List<String>> baseQuery = new ArrayList<>();
        List<String> masterQuery = new ArrayList<>();
        for (List<String> query :subqueries) {
            masterQuery.addAll(query);
        }
        baseQuery.add(masterQuery);

        *//*----------------------------*//*
        *//* --- SIMULATION: base case --- *//*
        *//*----------------------------*//*
        System.out.println("The base case query is " + masterQuery);
        List<Utility.UtilityGeneral.Pair> listedResults_base =
                SearchEngine.SimulatorUtility.produceRankedListFromBaseQuery(baseQuery, sizeOfRetrievedList, sizeOfFinalRankedList);
        // printing to the console
        Utility.UtilityConsolePrinting.printMyRankedList("The base case", listedResults_base);


        *//*----------------------------*//*
        *//* --- SIMULATION: trial case --- *//*
        *//*----------------------------*//*
        System.out.println("The trial case queries are " + subqueries);
        List<Utility.UtilityGeneral.Pair> listedRankedResults_trial =
                SearchEngine.SimulatorUtility.produceRankedListFromListOfQueries(
                subqueries, expansionMultiplier, sizeOfRetrievedList, sizeOfFinalRankedList);
        // printing to the console
        Utility.UtilityConsolePrinting.printMyRankedList("The trial case", listedRankedResults_trial);
    }*/
}
