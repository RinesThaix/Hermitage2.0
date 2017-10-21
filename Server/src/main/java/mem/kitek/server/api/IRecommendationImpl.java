package mem.kitek.server.api;

import mem.kitek.server.HallManager;
import mem.kitek.server.commons.Hall;
import mem.kitek.server.commons.HallCategory;
import mem.kitek.server.util.IRecommendation;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IRecommendationImpl implements IRecommendation {

    @Override
    public Set<Hall> getNecessaryHalls(Map<HallCategory, Float> probabilities) {
        Set<Hall> hallSet = new TreeSet<>();

        for (Map.Entry<HallCategory, Float> entry : probabilities.entrySet()) {
            if (entry.getValue() > 0) {
                hallSet.addAll(HallManager.getHallsByCategory(entry.getKey()));
            }
        }

        return hallSet;
    }
}
