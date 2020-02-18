import javax.json.JsonObject;
import java.util.List;

public class MeasureRank {

    public static List<Double> compareSimulationWithBaseLine(Settings settings, List<List<UtilityGeneral.Pair>> mySims ) throws Exception {
        // read queries from file
        JsonObject baseLinesFromFile = UtilityFileReader.readJsonFromFile(settings.getBaseLinePath());
        List<List<UtilityGeneral.Pair>> baseLines = UtilityFileReader.parseListOfSearchResults(baseLinesFromFile);

        // compare with statistical test
        /*return StatisticalRankComparision.compareAllCases(baseLines, mySims, settings.p);*/
        return null;
    }
}
