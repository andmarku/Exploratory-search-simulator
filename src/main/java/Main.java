import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Running");
        SimulatorSettings settings = new SimulatorSettings();
        settings.setStandardSettings();
        Simulator.mySimulator(settings);
        System.out.println("Finished");
    }
}// end of class