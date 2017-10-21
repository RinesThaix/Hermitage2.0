package mem.kitek.server.util;

import mem.kitek.server.commons.Hall;
import mem.kitek.server.commons.HallCategory;

import java.util.Map;
import java.util.Set;

/**
 * Created by RINES on 21.10.17.
 */
public interface IRecommendation {

    Set<Hall> getNecessaryHalls(Map<HallCategory, Float> probabilities);

}
