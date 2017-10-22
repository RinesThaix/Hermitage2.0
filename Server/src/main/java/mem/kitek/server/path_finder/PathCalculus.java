package mem.kitek.server.path_finder;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.Hall;
import mem.kitek.server.map.Graph;

import java.util.*;

/**
 * Created by RINES on 22.10.17.
 */
public class PathCalculus {

    public static List<Integer> getPath(Graph graph, int start, int end, Set<Integer> wanted) {
        return getPath(graph, start, end, wanted, Lists.newArrayList(), calcDists(graph));
    }

    private static List<Integer> getPath(Graph graph, int start, int end, Set<Integer> wanted, List<Integer> path, int[][] dists) {
        if(wanted.isEmpty()) {
            path.addAll(getPath0(graph, start, end, dists));
            return path;
        }
        Integer target = wanted.stream().min(Comparator.comparingInt(id -> dists[start][id])).orElse(null);
        if(target == null)
            throw new RuntimeException();
        wanted.remove(target);
        path.addAll(getPath0(graph, start, target, dists));
        return getPath(graph, target, end, wanted, path, dists);
    }

    private static List<Integer> getPath0(Graph graph, int from, int to, int[][] dists) {
        int n = graph.getEdgesMap().keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
        int[] d = new int[n];
        int[] path = new int[n];
        int max = Integer.MAX_VALUE >> 1;
        Arrays.fill(d, max);
        PriorityQueue<Edge> q = new PriorityQueue<>(Comparator.comparingInt(e -> e.a));
        q.add(new Edge(0, from));
        while(!q.isEmpty()) {
            int v = q.peek().b, cur_d = -q.peek().a;
            q.poll();
            if(cur_d > d[v])
                    continue;
            for(int t = 0; t < n; ++t) {
                if(t == v || dists[v][t] == max)
                    continue;
                int length = dists[v][t];
                if(d[v] + length < d[t]) {
                    d[t] = d[v] + length;
                    path[t] = v;
                    q.add(new Edge(-d[t], t));
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        int i = to;
        while(i != from) {
            result.add(i);
            i = path[i];
        }
        Collections.reverse(result);
        return result;
    }

    private static class Edge {

        private final int a, b;

        private Edge(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    private static int[][] calcDists(Graph graph) {
        int n = graph.getEdgesMap().keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
        int[][] d = new int[n][n];
        int max = Integer.MAX_VALUE;
        for(int i = 0; i < n; ++i)
            Arrays.fill(d[i], max);
        for(int i = 0; i < n; ++i) {
            Set<Integer> edges = graph.getEdgesFrom(i);
            Hall h1 = HallManager.getHall(i);
            if(edges != null)
                for(int j : edges) {
                    Hall h2 = HallManager.getHall(j);
                    if(h1 != null && h2 != null) {
                        int o1 = HallManager.getHallOnline(i);
                        if(o1 == -1)
                            o1 = 0;
                        int o2 = HallManager.getHallOnline(j);
                        if(o2 == -1)
                            o2 = 0;
                        d[i][j] = d[j][i] = o1 + o2 + 2;
                    }
                }
        }
        for (int k=0; k<n; ++k)
            for (int i=0; i<n; ++i)
                for (int j=0; j<n; ++j)
                    d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
        return d;
    }

}
