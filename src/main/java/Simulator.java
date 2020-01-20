import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.*;

class Simulator {

    static void loopingSimulator(SimulatorSettings settings) throws Exception {
        for (int i = 0; i < 3; i++) {
            // change settings

            // run simulation
            List<List<UtilityGeneral.Pair>> mySims = runSimulation(settings);

            // store resulting list
            List<JsonObject> simulationsAsListOfJsons = UtilityJsonCreator.createListOfJsonObjectsFromListOfListOfPairs(mySims);
            JsonArray trialCases = UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulationsAsListOfJsons);

            List<String> nameOfJsonsToStore = new ArrayList<>();
            nameOfJsonsToStore.add("trials_itr_nr_" + i);

            List<JsonValue> jsonsToStore = new ArrayList<>();
            jsonsToStore.add(trialCases);
            UtilityStoreInFile.storeResults(settings, settings.simulationName, nameOfJsonsToStore, jsonsToStore);
        }
    }

    static void mySimulator(SimulatorSettings settings) throws Exception {
        // run simulation
        List<List<UtilityGeneral.Pair>> mySims = runSimulation(settings);

        // score results
        List<Double> scores = compareSimulationWithBaseLine(settings, mySims);

        // store resulting list
        List<JsonObject> simulationsAsListOfJsons = UtilityJsonCreator.createListOfJsonObjectsFromListOfListOfPairs(mySims);
        JsonArray trialCases = UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulationsAsListOfJsons);

        List<String> nameOfJsonsToStore = new ArrayList<>();
        nameOfJsonsToStore.add("trials");
        nameOfJsonsToStore.add("scores");

        List<JsonValue> jsonsToStore = new ArrayList<>();
        jsonsToStore.add(trialCases);
        jsonsToStore.add(UtilityJsonCreator.createJsonObjectFromListDoubles(scores));

        UtilityStoreInFile.storeResults(settings, settings.simulationName, nameOfJsonsToStore, jsonsToStore);
    }

    private static List<Double> compareSimulationWithBaseLine(SimulatorSettings settings, List<List<UtilityGeneral.Pair>> mySims ) throws Exception {
        // read queries from file
        JsonObject baseLinesFromFile = UtilityFileReader.readJsonFromFile(settings.baseLinePath);
        List<List<UtilityGeneral.Pair>> baseLines = UtilityFileReader.parseListOfSearchResults(baseLinesFromFile);

        // compare with statistical test
        return StatisticalRankComparision.compareAllCases(baseLines, mySims, settings.p);
    }

    private static List<List<UtilityGeneral.Pair>> runSimulation(SimulatorSettings settings) throws Exception {
        // read queries from file
        JsonObject queriesFromFile = UtilityFileReader.readJsonFromFile(settings.queryPath);
        List<String> listOfQueryTerms = UtilityFileReader.readMasterQueries(queriesFromFile);

        // run simulation
        return SimulatorTrialCase.trialSimulator(settings, listOfQueryTerms);
    }

    static void baseSimulator(SimulatorSettings settings) throws Exception {
        // read in query terms
        JsonObject baseCaseFromFile = UtilityFileReader.readJsonFromFile(settings.queryPath);
        List<String> listOfQueryTerms = UtilityFileReader.readMasterQueries(baseCaseFromFile);

        // create base case searches
        List<JsonObject> simulatedBaseLines = SimulatorBaseCase.simulateBaseCase(settings, listOfQueryTerms);

        // store
        JsonArray baseCases = UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulatedBaseLines);

        List<String> nameOfJsonsToStore = new ArrayList<>();
        nameOfJsonsToStore.add("baseLines");

        List<JsonValue> jsonsToStore = new ArrayList<>();
        jsonsToStore.add(baseCases);

        UtilityStoreInFile.storeResults(settings, settings.baseLineName, nameOfJsonsToStore, jsonsToStore);
    }

    static void querySimulator(SimulatorSettings settings) throws Exception {
        int seed = (int) (Math.random()*100);
        System.out.println(seed);
        List<String> listOfQueryTerms = SimulatorQueryCreator.createAllMasterQueries(settings.numOfItr * settings.sizeOfFullQuery, seed);
        JsonArray queries = UtilityJsonCreator.createJsonArrayFromListOfStrings(listOfQueryTerms);

        List<String> nameOfJsonsToStore = new ArrayList<>();
        nameOfJsonsToStore.add("queryTerms");

        List<JsonValue> jsonsToStore = new ArrayList<>();
        jsonsToStore.add(queries);

        UtilityStoreInFile.storeResults(settings,  settings.queryName, nameOfJsonsToStore, jsonsToStore);
    }

}// end of simulator class
