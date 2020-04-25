package Settings;

import java.util.AbstractMap;
import java.util.List;

public interface Settings {
    String getScorePath();
    String getSimulationPath();
    String getQueryPath();
    String getSearchPath();
    int getNumOfItr();
    int getSizeOfQuery();
    int getSizeOfFinalRankedList();
    int getSizeOfRetrievedList();
    void setStandardSettings();
    List<AbstractMap<String, Double>> getParamCombs();
    List<Double> getValuesOfP();

}
