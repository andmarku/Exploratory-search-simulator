package Settings;

import java.util.ArrayList;
import java.util.List;

public class ThreeQueryTerms implements Settings{
    String simulationPath;
    String queryPath;
    String rbClusterPath;
    String rbSamplingPath;
    String rbOverlapPath;
    int numOfItr;
    int sizeOfQuery;
    int sizeOfRetrievedList;
    int maxSizeOfFinalList;
    int maxSizeToExpandToV1;
    List<Double> alphaList = new ArrayList<>();
    List<Double> betaList = new ArrayList<>();
    List<Double> etaList = new ArrayList<>();
    List<Double> gamma1List = new ArrayList<>();
    List<Double> gamma2List = new ArrayList<>();
    List<Double> valuesOfP = new ArrayList<>();
    List<Double> valuesOfInnerP = new ArrayList<>();
    List<List<Double>> parameterCombinations;

    public void setStandardSettings(){
        // general parameters
        numOfItr = 3;
        sizeOfQuery = 5;
        sizeOfRetrievedList = 500;
        maxSizeOfFinalList = 50;

        // set parameter values
        alphaList.add(-0.5);
        alphaList.add((double) 0);
        alphaList.add(0.5);
        alphaList.add((double) 1);

        betaList.add((double) 0);
        betaList.add(0.5);
        betaList.add((double) 1);

        gamma1List.add((double) 0);
        gamma1List.add(0.5);
        gamma1List.add((double) 1);

        // create all relevant combinations
        parameterCombinations = createAllParamCombinations(alphaList, betaList, gamma1List);

        // set values of p
        valuesOfP.add(0.85);
        valuesOfP.add(0.9);
        valuesOfP.add(0.95);


        // set values of inner p
        valuesOfInnerP.add(0.75);
        valuesOfInnerP.add(0.8);


        // set names
        String pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//threeQueryTerms//";
        String simulationName = "sims";
        String queryName = "queryTerms_2";

        String scoreName = "smallerTest_";

        // create paths
        rbClusterPath = pathToFolder + scoreName + "rbCluster";
        rbSamplingPath = pathToFolder + scoreName + "rbSampling";
        rbOverlapPath = pathToFolder + scoreName + "rbOverlap";
        simulationPath = pathToFolder + simulationName;
        queryPath = pathToFolder + queryName;
    }

    private static List<List<Double>> createAllParamCombinations(List<Double> alphaList, List<Double> betaList, List<Double> gamma1List){
        List<List<Double>> paramCombs = new ArrayList<>();
        for (double alpha : alphaList) {
            for (double beta : betaList) {
                for (double gamma1 : gamma1List) {
                    List<Double> singleParamComb = new ArrayList<>();
                    singleParamComb.add(alpha);
                    singleParamComb.add(beta);
                    singleParamComb.add(gamma1);
                    paramCombs.add(singleParamComb);
                }
            }
        }
        return paramCombs;
    }

    public String getRbClusterPath(){return rbClusterPath;}
    public String getRbSamplingPath(){return rbSamplingPath;}
    public String getRbOverlapPath(){return rbOverlapPath;}
    public String getQueryPath(){return queryPath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfQuery(){return sizeOfQuery;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public int getMaxSizeOfFinalList(){return maxSizeOfFinalList;}
    public int getMaxSizeToExpandToV1(){return maxSizeToExpandToV1;}
    public List<List<Double>> getParamCombs(){return parameterCombinations;}
    public List<Double> getValuesOfP(){return valuesOfP;}
    public List<Double> getValuesOfInnerP(){return valuesOfInnerP;}

}
