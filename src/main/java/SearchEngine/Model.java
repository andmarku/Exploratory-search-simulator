package SearchEngine;

import Settings.Settings;
import Utility.General;

import java.util.AbstractMap;
import java.util.List;

public interface Model {
    List<General.Pair> produceRankedList(Settings settings, double expMultiplier, List<AbstractMap<String, AbstractMap<String, Double>>> searchResultLists) throws Exception;
}
