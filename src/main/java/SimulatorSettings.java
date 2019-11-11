class SimulatorSettings {
    String pathToFolder;
    String simulationName;
    int numOfItr;
    int sizeOfFullQuery;
    int sizeOfFinalRankedList;
    int sizeOfRetrievedList;
    double baseCaseExpansionMultiplier;
    int baseCaseNumOfSubQueries;
    double expansionMultiplier;
    int numOfSubQueries;

    void setStandardSettings(){
        // path to store result in
        pathToFolder = "//home//fallman//testJava";
        simulationName = "firstAttempt";

        // general parameters
        numOfItr = 1;
        // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        sizeOfFullQuery = 2;
        sizeOfFinalRankedList = 10;
        sizeOfRetrievedList = 10;

        // base case
        baseCaseExpansionMultiplier = 0;
        baseCaseNumOfSubQueries = 1;

        // trial case
        expansionMultiplier = 2;
        // TODO: 2019-10-30 assumes that the query is divisible by the number of subqueries
        numOfSubQueries = SimulatorUtility.setNumOfSubQueries(2, sizeOfFullQuery);
    }
}
