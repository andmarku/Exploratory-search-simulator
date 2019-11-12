import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Running");
        SimulatorSettings settings = new SimulatorSettings();
        settings.setStandardSettings();

        Simulator.mySimulator(settings);


        List<List<String>> myQueries = new ArrayList<>();
        myQueries.add(new ArrayList<>());
        myQueries.add(new ArrayList<>());
        myQueries.get(0).add("Exploratory");
        myQueries.get(0).add("study");

        myQueries.get(1).add("index");
        SimulatorManualQueries.runManualQueries(myQueries, settings);

        System.out.println("Finished");
    }
}// end of class