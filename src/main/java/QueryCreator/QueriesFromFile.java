package QueryCreator;

import Settings.Settings;
import Utility.FileReader;

import javax.json.JsonObject;
import java.util.List;

public class QueriesFromFile {
    public static List<String> getQueriesFromFile(Settings settings) throws Exception {
        // read all queries from file
        List<JsonObject> listQueriesFromFile = FileReader.readJsonFromFile(settings.getQueryPath());
        if (listQueriesFromFile.size() > 1){
            System.out.println("Maybe reading in wrong file for the queries!");
        }

        JsonObject queriesFromFile = listQueriesFromFile.get(0);
        return QueryCreator.parseMasterQueries(queriesFromFile);
    }
}
