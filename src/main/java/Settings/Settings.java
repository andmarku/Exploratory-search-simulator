package Settings;

import java.util.List;

public interface Settings {
    String getPathToFolder();
    String getSimulationName();
    String getBaseLineName();
    String getQueryName();
    String getSimulationPath();
    String getBaseLinePath();
    String getQueryPath();
    int getNumOfItr();
    int getSizeOfFullQuery();
    int getSizeOfFinalRankedList();
    int getSizeOfRetrievedList();
    int getNumOfSubQueries();
    List<Double> getExpMultipliers();
    List<Integer> getSubqueries();

    void setStandardSettings();

}
