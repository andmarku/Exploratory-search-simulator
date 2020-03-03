package Settings;

import java.util.ArrayList;
import java.util.List;

public class ThreeQueryTerms implements Settings{
    String pathToFolder;
    String simulationName;
    String queryName;
    String simulationPath;
    String queryPath;
    int numOfItr;
    int sizeOfFullQuery;
    int sizeOfFinalRankedList;
    int sizeOfRetrievedList;
    List<Double> expMultipliers = new ArrayList<>();
    List<Integer>  sizeOfSubqueries = new ArrayList<>();

    public void setStandardSettings(){
        // path to store result in
        pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//threeQueryTerms//";
        simulationName = "firstSimulation";
        queryName = "newQueryTerms";
        simulationPath = pathToFolder + simulationName;
        queryPath = pathToFolder + queryName;

        // general parameters
        numOfItr = 5;
        sizeOfFinalRankedList =50;
        sizeOfRetrievedList = 1000;

        // simulation settings
        sizeOfFullQuery = 3;

        sizeOfSubqueries.add(1);
        sizeOfSubqueries.add(3);

        expMultipliers.add(-0.5);
        expMultipliers.add((double) 0);
        expMultipliers.add(0.5);
        expMultipliers.add((double) 1);
        expMultipliers.add((double) 2);
    }

    public String getPathToFolder(){return pathToFolder;}
    public String getSimulationName(){return simulationName;}
    public String getQueryName(){return queryName;}
    public String getSimulationPath(){return simulationPath;}
    public String getQueryPath(){return queryPath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfFullQuery(){return sizeOfFullQuery;}
    public int getSizeOfFinalRankedList(){return sizeOfFinalRankedList;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public List<Double> getExpMultipliers(){return expMultipliers;}
    public List<Integer> getSubqueries(){ return sizeOfSubqueries;}
}
