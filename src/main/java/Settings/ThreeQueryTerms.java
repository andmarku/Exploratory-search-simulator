package Settings;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThreeQueryTerms implements Settings{
    String simulationPath;
    String queryPath;
    String scorePath;
    int numOfItr;
    int sizeOfQuery;
    int sizeOfRetrievedList;
    int maxSizeOfFinalList;
    List<Double> alphaList = new ArrayList<>();
    List<Double> betaList = new ArrayList<>();
    List<Double> etaList = new ArrayList<>();
    List<Double> gamma1List = new ArrayList<>();
    List<Double> gamma2List = new ArrayList<>();
    List<Double> valuesOfP = new ArrayList<>();
    List<Double> valuesOfInnerP = new ArrayList<>();
    List<AbstractMap<String, Double>> parameterCombinations;

    public void setStandardSettings(){
        // general parameters
        numOfItr = 1000;
        sizeOfRetrievedList = 100;
        sizeOfQuery = 3;
        maxSizeOfFinalList = 50;

        // set parameter values
        alphaList.add(-0.5);
        alphaList.add((double) 0);
        alphaList.add(0.5);
        alphaList.add((double) 1);

        betaList.add((double) 0);
        betaList.add(0.5);
        betaList.add((double) 1);

        etaList.add((double) 0);
        etaList.add(0.5);
        etaList.add((double) 1);

        gamma1List.add((double) 1);
        gamma1List.add((double) 0);

        gamma2List.add((double) 1);
        gamma2List.add((double) 0);

        // create all relevant combinations
        parameterCombinations = createAllParamCombinations(alphaList, betaList, etaList, gamma1List, gamma2List);

        // set values of p
        valuesOfP.add(0.8);
        valuesOfP.add(0.9);

        // set values of inner p
        valuesOfInnerP.add(0.6);
        valuesOfInnerP.add(0.7);

        // set names
        String pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//threeQueryTerms//";
        String simulationName = "sims";
        String queryName = "queryTerms";
        String scoreName = "rboScores";

        // create paths
        simulationPath = pathToFolder + simulationName;
        queryPath = pathToFolder + queryName;
        scorePath = pathToFolder + scoreName;
    }

    private static List<AbstractMap<String, Double>> createAllParamCombinations(List<Double> alphaList, List<Double> betaList,
                                                                                List<Double> etaList, List<Double> gamma1List,
                                                                                List<Double> gamma2List){
        List<AbstractMap<String, Double>> paramCombs = new ArrayList<>();
        for (double alpha : alphaList) {
            for (double beta : betaList) {
                for (double eta : etaList) {
                    for (double gamma1 : gamma1List) {
                        for (double gamma2 : gamma2List) {
                            AbstractMap<String, Double> singleParamComb = new HashMap<>();
                            singleParamComb.put("a", alpha);
                            singleParamComb.put("b", beta);
                            singleParamComb.put("e", eta);
                            singleParamComb.put("g1", gamma1);
                            singleParamComb.put("g2", gamma2);
                            paramCombs.add(singleParamComb);
                        }
                    }
                }
            }
        }

        return paramCombs;
    }

    public String getQueryPath(){return queryPath;}
    public String getScorePath(){return scorePath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfQuery(){return sizeOfQuery;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public int getMaxSizeOfFinalList(){return maxSizeOfFinalList;}
    public List<AbstractMap<String, Double>> getParamCombs(){return parameterCombinations;}
    public List<Double> getValuesOfP(){return valuesOfP;}
    public List<Double> getValuesOfInnerP(){return valuesOfInnerP;}

}
