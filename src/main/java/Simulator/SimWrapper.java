package Simulator;

import Settings.Settings;

public class SimWrapper {
    public static void simulator(Settings settings) throws Exception {
        // generate queries
        System.out.println("Starting generating queries");
        QueryCreator.querySimulator(settings);
        System.out.println("Finished generating queries");

        // run simulations on the queries
        System.out.println("Starting with simulations");
        Looper.runAllCasesInLoop(settings);
        System.out.println("Finished simulations");
    }
}
