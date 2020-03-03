import Settings.*;
import Simulator.SimWrapper;
import Measures.MeasureWrapper;
import Utility.FileReader;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Running");

        Settings settings = new ThreeQueryTerms();
        settings.setStandardSettings();

        SimWrapper.simulator(settings);

        // Choose settings
        /*
        MeasureWrapper.compareAllCombinationsWithRBD(settings);
*/
        //ManualQueries.SimulatorManualQueries.runManualQueries(createMyQueries(), settings);

        System.out.println("Finished");
    }

    private static List<List<String>> createMyQueries(){
        List<List<String>> myQueries = new ArrayList<>();

        // query one
        myQueries.add(new ArrayList<>());
        myQueries.get(0).add("Exploratory");
        myQueries.get(0).add("study");

        // query two
        myQueries.add(new ArrayList<>());
        myQueries.get(1).add("index");

        return myQueries;
    }
}