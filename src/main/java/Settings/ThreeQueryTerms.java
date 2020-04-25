package Settings;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThreeQueryTerms implements Settings{
    String simulationPath;
    String queryPath;
    String scorePath;
    String searchPath;
    int numOfItr;
    int sizeOfQuery;
    int sizeOfFinalRankedList;
    int sizeOfRetrievedList;
    List<Double> alphaList = new ArrayList<>();
    List<Double> betaList = new ArrayList<>();
    List<Double> etaList = new ArrayList<>();
    List<Double> gamma1List = new ArrayList<>();
    List<Double> gamma2List = new ArrayList<>();
    List<AbstractMap<String, Double>> parameterCombinations;

    public void setStandardSettings(){
        // general parameters
        numOfItr = 1;
        sizeOfFinalRankedList = 5;
        sizeOfRetrievedList = 5;
        sizeOfQuery = 3;

        // set parameter values
        alphaList.add((double) 1);
        betaList.add((double) 1);
        etaList.add((double) 1);
        gamma1List.add((double) 1);
        gamma2List.add((double) 1);

        // create all relevant combinations
        parameterCombinations = createAllParamCombinations(alphaList, betaList, etaList, gamma1List, gamma2List);

        // set names
        String pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//";
        String simulationName = "sims";
        String queryName = "queryTerms";
        String scoreName = "rboScores";
        String searchName = "searchRes" + "QSize" + sizeOfQuery + "RLSize" + sizeOfRetrievedList;

        // create paths
        simulationPath = pathToFolder + simulationName;
        queryPath = pathToFolder + queryName;
        scorePath = pathToFolder + scoreName;
        searchPath = pathToFolder + searchName;
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
                            singleParamComb.put("alpha", alpha);
                            singleParamComb.put("beta", beta);
                            singleParamComb.put("eta", eta);
                            singleParamComb.put("gamma1", gamma1);
                            singleParamComb.put("gamma2", gamma2);
                            paramCombs.add(singleParamComb);
                        }
                    }
                }
            }
        }

        return paramCombs;
    }

    public String getSimulationPath(){return simulationPath;}
    public String getQueryPath(){return queryPath;}
    public String getScorePath(){return scorePath;}
    public String getSearchPath(){return searchPath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfQuery(){return sizeOfQuery;}
    public int getSizeOfFinalRankedList(){return sizeOfFinalRankedList;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public List<AbstractMap<String, Double>> getParamCombs(){return parameterCombinations;}

}
