package Settings;

import java.util.List;

public interface Settings {
    /*String getPathToFolder();
    String getSimulationName();
    String getQueryName();*/
    String getSimulationPath();
    String getQueryPath();
    String getScorePath();
    int getNumOfItr();
    int getSizeOfFullQuery();
    int getSizeOfFinalRankedList();
    int getSizeOfRetrievedList();
    List<Double> getExpMultipliers();
    List<Integer> getSubqueries();
    void setStandardSettings();
}
