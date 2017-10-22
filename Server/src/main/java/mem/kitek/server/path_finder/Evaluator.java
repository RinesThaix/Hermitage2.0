package mem.kitek.server.path_finder;

import mem.kitek.server.HallManager;
import mem.kitek.server.commons.Hall;
import mem.kitek.server.map.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Evaluator {
    private Graph graph;
    private Set<Hall> wanted;
    private static Random rnd = new Random("I dunno how to get info about what person likes".hashCode());

    private final static int GOOD_CAT_BONUS = 5;
    private final static int CROWD_BONUS = -5;
    private final static int CROWD_THRESHOLD = 10; // TODO use total room capacity to determine this
    private final static int DOUBLE_ENTRANCE_BONUS = -10;

    public Evaluator(Graph graph, Set<Hall> wanted) {
        this.graph = graph;
        this.wanted = wanted;
    }

    private boolean likes(int category) {
        return wanted.stream().anyMatch(hall -> hall.getCategory().getId() == category);
    }

    // impl note
    // reenter one node several times -- bad
    // a lot of people -- bad but ok
    // interesting -- good
    public int evaluate(Path path) {
        int score = 0;
        Set<Integer> used = new HashSet<>();
        for (Integer node : path.getData()) {
            try {
                if (likes(HallManager.getHallCategory(node).getId())) {
                    score += GOOD_CAT_BONUS;
                }
                if (HallManager.getHallOnline(node) > CROWD_THRESHOLD) {
                    score += CROWD_BONUS;
                }
                if (used.contains(node)) {
                    score += DOUBLE_ENTRANCE_BONUS;
                }
                used.add(node);
            } catch (NullPointerException e) {}
        }
        return score;
    }
}
