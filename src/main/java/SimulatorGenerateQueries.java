import javax.json.JsonArray;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;

public class SimulatorGenerateQueries {
    static void querySimulator(Settings settings) throws Exception {
        int seed = (int) (Math.random()*100);
        System.out.println("Seed for generating queries " + seed + ", making " +  settings.getNumOfItr() * settings.getSizeOfFullQuery() + " query terms");
        List<String> listOfQueryTerms = SimulatorQueryCreator.createAllMasterQueries(settings.getNumOfItr() * settings.getSizeOfFullQuery(), seed);
        JsonArray queries = UtilityJsonCreator.createJsonArrayFromListOfStrings(listOfQueryTerms);

        List<String> nameOfJsonsToStore = new ArrayList<>();
        nameOfJsonsToStore.add("queryTerms");

        List<JsonValue> jsonsToStore = new ArrayList<>();
        jsonsToStore.add(queries);

        UtilityStoreInFile.storeQueries(settings,  settings.getQueryName(), nameOfJsonsToStore, jsonsToStore);
    }
}
