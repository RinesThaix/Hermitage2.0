package mem.kitek.server.path_finder;

import mem.kitek.server.map.Node;

import java.util.ArrayList;
import java.util.List;

public class Path {
    List<Integer> path;

    public Path() {
        path = new ArrayList<>();
    }

    public Path(List<Integer> path) {
        this.path = path;
    }

    public List<Integer> getData() {
        return path;
    }
}
