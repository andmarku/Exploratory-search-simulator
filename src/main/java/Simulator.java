import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.*;

class Simulator {

    static void mySimulator(SimulatorSettings settings) throws Exception {

        // read stored base case from file
        String path = UtilityJsonPrinter.createCompleteFileName(settings.pathToFolder, settings.baseCaseName);
        JsonObject baseCaseFromFile = UtilityFileReader.readJsonFromFile(path);

        List<> simulatedBaseCase = baseCaseFromFile.getJsonArray("baseCases");

        // RUN SIMULATION
        List<String> masterQueries = UtilityFileReader.readMasterQueries(baseCaseFromFile);
        List<JsonObject> simulatedTrialCases = SimulatorTrialCase.trialSimulator(settings, masterQueries);

        List<> scores = StatisticalRankComparision.compareAllCases(List<List<UtilityGeneral.Pair>> listOfOrderedListsOne,
                simulatedTrialCases);

        //----------------
        // --- STORING ---
        //----------------
        Map<String, JsonValue> storageMap = new HashMap<>();

        // store settings
        storageMap.put("settings", UtilityJsonCreator.createJsonObjectFromSettings(settings));

        // store trial cases
        JsonArray trialCases = UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulatedTrialCases);
        storageMap.put("trialCases", trialCases);

        // save all stored jsons to file
        JsonObject jsonToPrint = UtilityJsonCreator.createJsonFromMapOfJsonsForPrinting(storageMap);
        UtilityJsonPrinter.writeJsonToFile(jsonToPrint, settings.pathToFolder, settings.simulationName);
    }

    static void baseSimulator(SimulatorSettings settings) throws Exception {
        Map<String, JsonValue> storageMap = new HashMap<>();

        // store settings
        storageMap.put("settings", UtilityJsonCreator.createJsonObjectFromSettings(settings));

        // create all query terms and store them
        List<String> listOfQueryTerms = SimulatorQueryCreator.createAllMasterQueries(settings.numOfItr * settings.sizeOfFullQuery, 0);
        JsonArray queries = UtilityJsonCreator.createJsonArrayFromListOfStrings(listOfQueryTerms);
        storageMap.put("queryTerms", queries);

        // create base case searches and store them
        List<JsonObject> simulatedBaseCases = SimulatorBaseCase.simulateBaseCase(settings, listOfQueryTerms);
        JsonArray baseCases = UtilityJsonCreator.createJsonArrayFromListOfJsonObjects(simulatedBaseCases);
        storageMap.put("baseCases", baseCases);

        // save all stored jsons to file
        JsonObject jsonToPrint = UtilityJsonCreator.createJsonFromMapOfJsonsForPrinting(storageMap);
        UtilityJsonPrinter.writeJsonToFile(jsonToPrint, settings.pathToFolder, settings.baseCaseName);
    }

}// end of simulator class
