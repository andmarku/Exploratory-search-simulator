import Measures.RankBiasedClusters;
import Measures.RankBiasedOverlap;
import Settings.*;
import Measures.MeasuresWrapper;
import Retriever.NewRetriver;
import Simulator.SimWrapper;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Running");

        // Choose settings
        Settings settings = new ThreeQueryTerms();
        settings.setStandardSettings();

        NewRetriver.queryTest();

    /*    List<String> ls = new ArrayList<>();
        ls.add("6bccc6a15bb56c459cd9b24858f3d4db139912e5");
        ls.add("a1bb2712c5cd51c6918bd19ac84eb1af88325189");
        ls.add("b4d9b7a2800c0030cc2468790ba28cf370b59476");
        NewRetriver.multiGet(ls);*/

        //RankBiasedClusters.run();
        //SimWrapper.simulator(settings);

        //MeasuresWrapper.compareWithRBD(settings);

        //MeasuresWrapper.run2(settings);

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