package mem.kitek.test;

import mem.kitek.server.map.Graph;
import mem.kitek.server.path_finder.Annealer;
import mem.kitek.server.path_finder.Path;

public class AnnealingTest {
    public static void main(String[] args) {
        Graph graph = new Graph();
        init(graph);
        Annealer annealer = new Annealer(graph);
        Path path = annealer.run();
        System.out.println(path.getData());
    }

    private static void init(Graph g) {
        g.edge(0, 1);
        g.edge(1, 2);
        g.edge(2, 3);
        g.edge(3, 4);
        g.edge(4, 5);
        g.edge(5, 6);
    }
}
