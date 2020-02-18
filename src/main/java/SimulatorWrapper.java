class SimulatorWrapper {
    static void simulator() throws Exception {
        // OBS!! currently I must remove the query file in folder since I use append and am not sure if I want to write over anyway.


        // Choose settings
        Settings settings = new SettingsThreeQueryTerms();
        settings.setStandardSettings();

        // generate queries
        SimulatorGenerateQueries.querySimulator(settings);
        System.out.println("Finished generating queries");

        // run simulations on the queries
        SimulatorLooper.runAllCasesInLoop(settings);

        // run measures
        /*MeasureRank.compareSimulationWithBaseLine(settings);*/
    }
}
