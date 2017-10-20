package mem.kitek.server.map;

/**
 * На самом деле, это коридор.
 * Created by RINES on 20.10.17.
 */
public class Edge {

    private final Node first;
    private final Node second;

    public Edge(Node first, Node second) {
        this.first = first;
        this.second = second;
    }

    public Node getFirst() {
        return this.first;
    }

    public Node getSecond() {
        return this.second;
    }

}
