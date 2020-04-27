package Settings;

import java.util.AbstractMap;
import java.util.List;

public interface Settings {
    String getScorePath();
    String getQueryPath();
    int getNumOfItr();
    int getSizeOfQuery();
    int getSizeOfRetrievedList();
    int getMaxSizeOfFinalList();
    void setStandardSettings();
    List<AbstractMap<String, Double>> getParamCombs();
    List<Double> getValuesOfP();
    List<Double> getValuesOfInnerP();
}
