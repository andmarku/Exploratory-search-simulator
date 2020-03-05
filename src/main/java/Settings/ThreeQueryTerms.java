package Settings;

import java.util.ArrayList;
import java.util.List;

public class ThreeQueryTerms implements Settings{
    String simulationPath;
    String queryPath;
    String scorePath;
    int numOfItr;
    int sizeOfFullQuery;
    int sizeOfFinalRankedList;
    int sizeOfRetrievedList;
    List<Double> expMultipliers = new ArrayList<>();
    List<Integer>  sizeOfSubqueries = new ArrayList<>();

    public void setStandardSettings(){
        // set names
        String pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//threeQueryTerms//";
        String simulationName = "firstSimulation";
        String queryName = "newQueryTerms";
        String scoreName = "rboScores";

        // create paths
        simulationPath = pathToFolder + simulationName;
        queryPath = pathToFolder + queryName;
        scorePath = pathToFolder + scoreName;

        // general parameters
        numOfItr = 2;
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

    public String getSimulationPath(){return simulationPath;}
    public String getQueryPath(){return queryPath;}
    public String getScorePath(){return scorePath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfFullQuery(){return sizeOfFullQuery;}
    public int getSizeOfFinalRankedList(){return sizeOfFinalRankedList;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public List<Double> getExpMultipliers(){return expMultipliers;}
    public List<Integer> getSubqueries(){ return sizeOfSubqueries;}
}
