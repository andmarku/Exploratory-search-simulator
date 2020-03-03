package Simulator;

import Settings.Settings;
import Settings.ThreeQueryTerms;

public class SimWrapper {
    public static void simulator() throws Exception {
        // OBS!! currently I must remove the query file in folder since I use append and am not sure if I want to write over anyway.


        // Choose settings
        Settings settings = new ThreeQueryTerms();
        settings.setStandardSettings();

        // generate queries
        QueryCreator.querySimulator(settings);
        System.out.println("Finished generating queries");

        // run simulations on the queries
        Looper.runAllCasesInLoop(settings);

        // run measures
        /*Measures.MeasureRank.compareSimulationWithBaseLine(settings);*/
    }
}
