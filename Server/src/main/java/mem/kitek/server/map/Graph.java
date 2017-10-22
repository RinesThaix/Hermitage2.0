package mem.kitek.server.map;

import mem.kitek.server.HallManager;
import mem.kitek.server.commons.Hall;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * На самом деле, Эрмитаж.
 * Created by RINES on 20.10.17.
 */
public class Graph {

    private final Map<Integer, Set<Integer>> edges = new HashMap<>();

    /**
     * Получить номера залов, в которые можно попасть из данного зала.
     * @param vertex номер данного зала.
     * @return сет номеров залов, в которые можно попасть из данного зала.
     */
    public Set<Integer> getEdgesFrom(int vertex) {
        return this.edges.get(vertex);
    }

    public Map<Integer, Set<Integer>> getEdgesMap() {
        return this.edges;
    }

    /**
     * Добавить проход между двумя залами.
     * @param a идентификатор первого зала.
     * @param b идентификатор второго зала.
     */
    public void edge(int a, int b) {
        edge0(a, b);
        edge0(b, a);
    }

    /**
     * DEMO
     */
    public void prepare() {
        for(Map.Entry<Integer, Set<Integer>> entry : new HashMap<>(this.edges).entrySet()) {
            Hall hall = HallManager.getHall(entry.getKey());
            if(hall == null || hall.getFloor() != 1) {
                this.edges.remove(entry.getKey());
                continue;
            }
            new HashSet<>(entry.getValue()).forEach(id -> {
                Hall h = HallManager.getHall(id);
                if(h == null || h.getFloor() != 1)
                    this.edges.get(entry.getKey()).remove(id);
            });
        }
    }

    private void edge0(int a, int b) {
        Set<Integer> set = this.edges.get(a);
        if(set == null) {
            set = new HashSet<>();
            this.edges.put(a, set);
        }
        set.add(b);
    }

}
