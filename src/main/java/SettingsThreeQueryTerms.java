import java.util.ArrayList;
import java.util.List;

class SettingsThreeQueryTerms implements Settings{
    String pathToFolder;
    String simulationName;
    String baseLineName;
    String queryName;
    String simulationPath;
    String baseLinePath;
    String queryPath;
    int numOfItr;
    int sizeOfFullQuery;
    int sizeOfFinalRankedList;
    int sizeOfRetrievedList;
    int numOfSubQueries;
    List<Double> expMultipliers = new ArrayList<>();
    List<Integer>  sizeOfSubqueries = new ArrayList<>();

    public void setStandardSettings(){
        // path to store result in
        pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//threeQueryTerms//";
        simulationName = "firstSimulation";
        baseLineName = "firstBaseLineCase";
        queryName = "newQueryTerms";
        simulationPath = pathToFolder + simulationName;
        baseLinePath = pathToFolder + baseLineName;
        queryPath = pathToFolder + queryName;

        // general parameters
        numOfItr = 100;
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
    public String getBaseLineName(){return baseLineName;}
    public String getQueryName(){return queryName;}
    public String getSimulationPath(){return simulationPath;}
    public String getBaseLinePath(){return baseLinePath;}
    public String getQueryPath(){return queryPath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfFullQuery(){return sizeOfFullQuery;}
    public int getSizeOfFinalRankedList(){return sizeOfFinalRankedList;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public int getNumOfSubQueries(){return numOfSubQueries;}
    public List<Double> getExpMultipliers(){return expMultipliers;}
    public List<Integer> getSubqueries(){ return sizeOfSubqueries;}
}
