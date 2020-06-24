package Simulator;

import QueryCreator.QueryCreator;
import QueryCreator.QueriesFromFile;
import Settings.Settings;
import Settings.ThreeQueryTerms;

import java.util.List;

public class SimWrapper {
    public static void run() throws Exception{
        System.out.println("Running");

        // Choose settings
        Settings settings = new ThreeQueryTerms();
        settings.setStandardSettings();
        settings.setBetaAsInterpolation();

        for (Integer nrOfQueryTerms : settings.getQuerySpecificityToTest()) {

            if (nrOfQueryTerms < 7){
                continue;
            }

            settings.changeNumberOfQueryTerms(nrOfQueryTerms);

            //SimWrapper.generateSearches(settings);

            SimWrapper.simulator(settings);
        }
        System.out.println("Finished");
    }

    public static void generateSearches(Settings settings) throws Exception {
        // generate queries
        System.out.println("Starting generating queries");
        QueryCreator.querySimulator(settings);
        System.out.println("Finished generating queries");
    }

    public static void simulator(Settings settings) throws Exception {
        System.out.println("Reading in queries from file");
        List<String> listOfQueryTerms  = QueriesFromFile.getQueriesFromFile(settings);

        System.out.println("Starting with simulations");
        Simulator.runAllIterations(settings, listOfQueryTerms);
    }


}
