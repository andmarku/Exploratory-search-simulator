package Measures;

import Utility.FileReader;
import Utility.General;
import Settings.Settings;
import javax.json.JsonObject;
import java.util.List;

public class MeasureRank {

    public static List<Double> compareSimulationWithBaseLine(Settings settings, List<List<General.Pair>> mySims ) throws Exception {
        // read queries from file
        JsonObject baseLinesFromFile = FileReader.readJsonFromFile(settings.getBaseLinePath());
        List<List<General.Pair>> baseLines = FileReader.parseListOfSearchResults(baseLinesFromFile);

        // compare with statistical test
        /*return StatisticalRankComparision.compareAllCases(baseLines, mySims, settings.p);*/
        return null;
    }
}
