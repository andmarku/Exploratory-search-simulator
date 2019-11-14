class SimulatorSettings {
    String pathToFolder;
    String simulationName;
    int numOfItr;
    int sizeOfFullQuery;
    int sizeOfFinalRankedList;
    int sizeOfRetrievedList;
    double baseCaseExpansionMultiplier;
    double expansionMultiplier;
    int numOfSubQueries;

    void setStandardSettings(){
        // path to store result in
        pathToFolder = "//home//fallman//development//dataForThesis//storedSimulationResults//";
        simulationName = "firstAttempt";

        // general parameters
        numOfItr = 1;
        sizeOfFullQuery = 6;
        sizeOfFinalRankedList = 10;
        sizeOfRetrievedList = 1000;

        // trial case
        expansionMultiplier = 2;
        numOfSubQueries = 2;

        // Check that the number of sub-queries are compatible with the size of the full query
        checkNumOfSubQueries(numOfSubQueries, sizeOfFullQuery);
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
