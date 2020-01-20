class SimulatorSettings {
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
    double expansionMultiplier;
    int numOfSubQueries;
    double p;

    void setStandardSettings(){
        // path to store result in
        pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//";
        simulationName = "firstSimulation";
        baseLineName = "firstBaseLineCase";
        queryName = "firstQueryTerms";
        simulationPath = pathToFolder + simulationName;
        baseLinePath = pathToFolder + baseLineName;
        queryPath = pathToFolder + queryName;

        // general parameters
        numOfItr = 50;
        sizeOfFullQuery = 6;
        sizeOfFinalRankedList = 100;
        sizeOfRetrievedList = 1000;
        p = 0.9;

        // trial case
        expansionMultiplier = 0.1;
        numOfSubQueries = 2;

        // Check that the number of sub-queries are compatible with the size of the full query
        checkNumOfSubQueries(numOfSubQueries, sizeOfFullQuery);
    }

    void setSimulationName(String simulationName){
        this.simulationName = simulationName;
        simulationPath = pathToFolder + simulationName;
    }
    void setSizeOfFullQuery(int sizeOfFullQuery){
        this.sizeOfFullQuery = sizeOfFullQuery;
        // Check that the number of sub-queries are compatible with the size of the full query
        checkNumOfSubQueries(numOfSubQueries, sizeOfFullQuery);
    }
    void setSizeOfFinalRankedList(int sizeOfFinalRankedList){
        this.sizeOfFinalRankedList = sizeOfFinalRankedList;
    }
    void setSizeOfRetrievedList(int sizeOfRetrievedList){
        this.sizeOfRetrievedList = sizeOfRetrievedList;
    }
    void setValueOfP(int p){
        this.p = p;
    }
    void setExpansionMultiplier(double expansionMultiplier){
        this.expansionMultiplier = expansionMultiplier;
    }
    void setNumOfSubQueries(int numOfSubQueries){
        this.numOfSubQueries = numOfSubQueries;
    }

    private static void checkNumOfSubQueries(int numOfSubQueries, int sizeOfFullQuery){
        // the query is assumed to be divisible by the number of subqueries
        if(numOfSubQueries > sizeOfFullQuery){
            System.out.println();
            System.out.println(" ------------ OBS! OBS! OBS! OBS! ------------");
            System.out.println("\t The number of subqueries are larger than the size of the full query.");
            System.out.println("\t The size of the full query is " + sizeOfFullQuery + ".");
            System.out.println("\t and the number of subqueries are " + numOfSubQueries + ".");
            System.out.println(" ------------ OBS! OBS! OBS! OBS! ------------");
            System.out.println();
            return;
        }

        if(sizeOfFullQuery % numOfSubQueries != 0){
            System.out.println();
            System.out.println(" ------------ OBS! OBS! OBS! OBS! ------------");
            System.out.println("\t The size of the full query is not divisible by the number of subqueries.");
            System.out.println("\t The size of the full query is " + sizeOfFullQuery + ".");
            System.out.println("\t and the number of subqueries are " + numOfSubQueries + ".");
            System.out.println(" ------------ OBS! OBS! OBS! OBS! ------------");
            System.out.println();
        }

    }
}
