package mem.kitek.server.path_finder;

import mem.kitek.server.map.Graph;

import java.util.*;

public class Annealer {
    private static final double INITIAL_TEMP = 1000;
    private static final double TEMP_THRESHOLD = 0.01;
    private static final int MAX_PATH_LEN = 100;
    private double temp = INITIAL_TEMP;
    private Evaluator evaluator;
    private Graph graph;
    private Random rnd = new Random("я устал, почему это не работает?".hashCode());

    public Annealer(Graph graph) {
        evaluator = new Evaluator(graph);
        this.graph = graph;
    }

    public Path run() {
        Path curr = randomPath(graph);
        while (temp > TEMP_THRESHOLD) {
            int dist = (int) (MAX_PATH_LEN * temp / INITIAL_TEMP);
            Path newPath = mutate(curr, dist);
            double oldScore = evaluator.evaluate(curr);
            double newScore = evaluator.evaluate(newPath);
            if (newScore > oldScore) {
                curr = newPath;
            } else {
                double p = temp / INITIAL_TEMP * 0.2;
                if (p < rnd.nextDouble()) {
                    curr = newPath;
                }
            }
            coolDown();
        }
        return curr;
    }

    private void coolDown() {
        temp--;
    }

    private Path mutate(Path curr, int dist) {
        List<Integer> path = curr.getData();
        int midVertex = rnd.nextInt(path.size());
        int newVertex = randomVertex(graph, path.get(midVertex), dist);
        Path newPath = rebuildPath(graph, curr, path.get(midVertex), newVertex);
        return newPath;
    }

    private Optional<Path> findPath(Graph graph, int start, int end) {
        Queue<Integer> queue = new ArrayDeque<>();
        List<Integer> path = new ArrayList<>();
        Set<Integer> used = new HashSet<>();
        queue.add(start);
        boolean found = false;
        while (!queue.isEmpty()) {
            int next = queue.poll();
            found = bfs(path, queue, graph, next, end, used);
            if (found) {
                break;
            }
        }
        if (found) {
            return Optional.of(new Path(path));
        }
        return Optional.empty();
    }

    private boolean bfs(List<Integer> currPath, Queue<Integer> queue, Graph graph, int curr, int target, Set<Integer> used) {
        currPath.add(curr);
        if (curr == target) {
            return true;
        }
        used.add(curr);
        for (Integer next : graph.getEdgesFrom(curr)) {
            if (!used.contains(next)) {
                queue.add(next);
            }
        }
        return false;
    }

    /**
     * Rebuild path so that is passes through newVertex
     *
     * @param path
     * @param oldVertex separator for the path when choosing starting vertex
     * @param newVertex
     * @return
     */
    private Path rebuildPath(Graph graph, Path path, int oldVertex, int newVertex) {
        int ind = path.getData().indexOf(oldVertex); // TODO check if it is not found
        int maxRetryCnt = 100; // in case if something breaks we will not loop forever
        // find left part
        List<Integer> newPath = new ArrayList<>();
        for (int attempt = 0; attempt < maxRetryCnt; attempt++) {
            // does it work if old vertex is the first or the last?
            int middleIndex = rnd.nextInt(ind + 1);
            Optional<Path> middleToNew = findPath(graph, path.getData().get(middleIndex), newVertex);
            if (!middleToNew.isPresent()) {
                continue;
            }
            newPath.addAll(path.getData().subList(0, middleIndex));
            newPath.addAll(middleToNew.get().getData());
            break;
        }
        for (int attempt = 0; attempt < maxRetryCnt; attempt++) {
            // does it work if old vertex is the first or the last?
            int middleIndex = rnd.nextInt(path.getData().size() - ind); // +1?
            Optional<Path> newToMiddle = findPath(graph, newVertex, path.getData().get(ind + middleIndex));
            if (!newToMiddle.isPresent()) {
                continue;
            }
            newPath.addAll(newToMiddle.get().getData());
            newPath.addAll(path.getData().subList(ind + middleIndex, path.getData().size()));
            break;
        }
        return new Path(newPath);
    }

    /**
     * Find a random vertex not further than dist from curr vertex
     *
     * @param graph
     * @param curr
     * @param dist
     * @return
     */
    private int randomVertex(Graph graph, int curr, int dist) {
        for (int i = 0; i < dist; i++) {
            Set<Integer> edges = graph.getEdgesFrom(curr);
            curr = randomElement(edges);
        }
        return curr;
    }

    private Path randomPath(Graph graph) {
        List<Integer> path = new ArrayList<>();
        path.add(0);
        int len = rnd.nextInt(MAX_PATH_LEN / 2) + MAX_PATH_LEN / 2;
        for (int i = 0; i < len; i++) {
            Set<Integer> edges = graph.getEdgesFrom(path.get(path.size() - 1));
            Integer next = randomElement(edges);
            path.add(next);
        }
        return new Path(path);
    }

    private static <T> T randomElement(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t: coll) {
            if (--num < 0) {
                return t;
            }
        }
        throw new AssertionError();
    }
}
