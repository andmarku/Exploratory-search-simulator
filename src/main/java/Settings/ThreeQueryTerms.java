package Settings;

import java.util.ArrayList;
import java.util.List;

public class ThreeQueryTerms implements Settings{
    String queryPath;
    String pathToFolder;
    String queryName;
    int numOfItr;
    int sizeOfQuery;
    int sizeOfRetrievedList;
    int maxSizeOfFinalList;
    int maxSizeToExpandToV1;
    List<Double> alphaList = new ArrayList<>();
    List<Double> betaList = new ArrayList<>();
    List<Double> gamma1List = new ArrayList<>();
    List<Double> valuesOfP = new ArrayList<>();
    List<Double> valuesOfInnerP = new ArrayList<>();
    List<Integer> getQuerySpecificityToTest = new ArrayList<>();
    List<List<Double>> parameterCombinations;


    public void setStandardSettings(){
        // general parameters
        numOfItr = 200;
        sizeOfRetrievedList = 40;
        maxSizeOfFinalList = 50;

        // set the sizes of the query to examine
        getQuerySpecificityToTest.add(3);
        getQuerySpecificityToTest.add(5);
        getQuerySpecificityToTest.add(7);

        // set parameter values
        alphaList.add((double) 0);
        alphaList.add(0.25);
        alphaList.add(0.5);
        alphaList.add(0.75);
        alphaList.add(0.9);
        alphaList.add(0.975);
        alphaList.add((double) 1);

        betaList.add((double) 0);
        betaList.add(0.025);
        betaList.add(0.1);
        betaList.add(0.25);
        betaList.add(0.5);
        betaList.add(0.75);
        betaList.add((double) 1);

        gamma1List.add((double) 0);
        gamma1List.add(0.025);
        gamma1List.add(0.1);
        gamma1List.add(0.25);
        gamma1List.add(0.5);
        gamma1List.add(0.75);
        gamma1List.add((double) 1);

        // create all relevant combinations
        parameterCombinations = createAllParamCombinations(alphaList, betaList, gamma1List);

        // set values of p
        valuesOfP.add(0.8);
        valuesOfP.add(0.9);
        valuesOfP.add(0.95);

        // set values of inner p
        valuesOfInnerP.add(0.7);
        valuesOfInnerP.add(0.8);

        // set path to store the results in
        pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//threeQueryTerms//";
    }

    public void setBetaAsInterpolation(){
        List<List<Double>> paramCombs = new ArrayList<>();

        List<Double> singleParamComb = new ArrayList<>();
        singleParamComb.add((double) 0); singleParamComb.add((double) 1); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.25); singleParamComb.add(0.75); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.5); singleParamComb.add(0.5); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.75); singleParamComb.add(0.25); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.9); singleParamComb.add(0.1); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.95); singleParamComb.add(0.05); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.975); singleParamComb.add(0.025); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.99); singleParamComb.add(0.01); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.995); singleParamComb.add(0.005); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add((double) 1); singleParamComb.add((double) 0); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);


        // GAMMA
        singleParamComb = new ArrayList<>();
        singleParamComb.add((double) 0); singleParamComb.add((double) 0); singleParamComb.add((double) 1);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.25); singleParamComb.add((double) 0); singleParamComb.add(0.75);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.5); singleParamComb.add((double) 0); singleParamComb.add(0.5);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.75); singleParamComb.add((double) 0); singleParamComb.add(0.25);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.9); singleParamComb.add((double) 0); singleParamComb.add(0.1);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.975); singleParamComb.add((double) 0); singleParamComb.add(0.025);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add((double) 1); singleParamComb.add((double) 0); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        // create all relevant combinations
        parameterCombinations = paramCombs;
    }

    public void setGammaAsInterpolation(){
        List<List<Double>> paramCombs = new ArrayList<>();

        List<Double> singleParamComb = new ArrayList<>();
        singleParamComb.add((double) 0); singleParamComb.add((double) 0); singleParamComb.add((double) 1);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.25); singleParamComb.add((double) 0); singleParamComb.add(0.75);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.5); singleParamComb.add((double) 0); singleParamComb.add(0.5);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.75); singleParamComb.add((double) 0); singleParamComb.add(0.25);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.9); singleParamComb.add((double) 0); singleParamComb.add(0.1);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add(0.975); singleParamComb.add((double) 0); singleParamComb.add(0.025);
        paramCombs.add(singleParamComb);

        singleParamComb = new ArrayList<>();
        singleParamComb.add((double) 1); singleParamComb.add((double) 0); singleParamComb.add((double) 0);
        paramCombs.add(singleParamComb);

        // create all relevant combinations
        parameterCombinations = paramCombs;
    }



    public void changeNumberOfQueryTerms(int newNr){
        sizeOfQuery = newNr;
        queryName = "queryTerms_" + sizeOfQuery;
        queryPath = pathToFolder + queryName;
    }
    public String getRbClusterPath(){return pathToFolder + "rbCluster";}
    public String getRbSamplingPath(){return pathToFolder + "rbSampling";}
    public String getRbOverlapPath(){return pathToFolder + "rbOverlap";}
    public String getQueryPath(){return queryPath;}
    public int getNumOfItr(){return numOfItr;}
    public int getSizeOfQuery(){return sizeOfQuery;}
    public int getSizeOfRetrievedList(){return sizeOfRetrievedList;}
    public int getMaxSizeOfFinalList(){return maxSizeOfFinalList;}
    public int getMaxSizeToExpandToV1(){return maxSizeToExpandToV1;}
    public List<List<Double>> getParamCombs(){return parameterCombinations;}
    public List<Double> getValuesOfP(){return valuesOfP;}
    public List<Double> getValuesOfInnerP(){return valuesOfInnerP;}
    public List<Integer> getQuerySpecificityToTest(){return getQuerySpecificityToTest;}

    private static List<List<Double>> createAllParamCombinations(List<Double> alphaList, List<Double> betaList, List<Double> gamma1List){
        List<List<Double>> paramCombs = new ArrayList<>();
        for (double alpha : alphaList) {
            for (double beta : betaList) {
                for (double gamma1 : gamma1List) {
                    // do not add all parameters as zero
                    if (alpha == beta){
                        if (alpha == gamma1){
                            if (alpha == 0){
                                continue;
                            }
                        }
                    }
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
}
