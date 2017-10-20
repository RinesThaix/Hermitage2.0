package mem.kitek.server.map;

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

    /**
     * Добавить проход между двумя залами.
     * @param a идентификатор первого зала.
     * @param b идентификатор второго зала.
     */
    public void edge(int a, int b) {
        edge0(a, b);
        edge0(b, a);
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
