package SearchEngine;

import Settings.Settings;
import Utility.General;

import java.util.AbstractMap;
import java.util.List;

public interface Model {
    List<General.Pair> produceExpandedRankedList(Settings settings, double expMultiplier, AbstractMap<String, AbstractMap<String, Double>> searchResult);
}
