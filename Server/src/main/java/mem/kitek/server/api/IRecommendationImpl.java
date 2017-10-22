package mem.kitek.server.api;

import mem.kitek.server.HallManager;
import mem.kitek.server.commons.Hall;
import mem.kitek.server.commons.HallCategory;
import mem.kitek.server.util.IRecommendation;

import java.util.*;
import java.util.stream.Collectors;

public class IRecommendationImpl implements IRecommendation {

    @Override
    public Set<Hall> getNecessaryHalls(Map<HallCategory, Float> probabilities) {
        return probabilities.entrySet().stream().filter(e -> e.getValue() > 0F)
                .map(e -> HallManager.getHallsByCategory(e.getKey()))
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
