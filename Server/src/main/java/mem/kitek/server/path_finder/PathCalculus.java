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
            addPath0(path, graph, start, end);
            return path;
        }
        Integer target = wanted.stream().min(Comparator.comparingInt(id -> dists[start][id])).orElse(null);
        if(target == null)
            throw new RuntimeException();
        wanted.remove(target);
        addPath0(path, graph, start, target);
        return getPath(graph, target, end, wanted, path, dists);
    }

    private static void addPath0(List<Integer> path, Graph graph, int from, int to) {
        List<Integer> path0 = getPath0(graph, from, to);
        if(!path.isEmpty()) {
            if(path.get(path.size() - 1) != path0.get(0))
                path.add(path0.get(0));
        }else
            path.add(path0.get(0));
        for(int i = 1; i < path0.size(); ++i)
            path.add(path0.get(i));
    }

    private static List<Integer> getPath0(Graph graph, int from, int to) {
        int n = graph.getEdgesMap().keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
        int[] d = new int[n];
        int[] path = new int[n];
        Arrays.fill(path, -1);
        int max = Integer.MAX_VALUE >> 1;
        Arrays.fill(d, max);
        d[from] = 0;
        PriorityQueue<Edge> q = new PriorityQueue<>(Comparator.comparingInt(e -> e.a));
        q.add(new Edge(0, from));
        while(!q.isEmpty()) {
            int v = q.peek().b, cur_d = -q.peek().a;
            q.poll();
            if(cur_d > d[v])
                    continue;
            for(int t : graph.getEdgesFrom(v)) {
                int dist = getDist(t, v);
                if(d[v] + dist < d[t]) {
                    d[t] = d[v] + dist;
                    path[t] = v;
                    q.add(new Edge(-d[t], t));
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        int i = to;
        while(i != from) {
            System.out.println("--" + i);
            result.add(i);
            i = path[i];
            if(i == -1)
                break;
        }
        result.add(from);
        Collections.reverse(result);
        System.out.println(from + " " + to + " " + result);
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
                        d[i][j] = d[j][i] = getDist(i, j);
                    }
                }
        }
        for (int k=0; k<n; ++k)
            for (int i=0; i<n; ++i)
                for (int j=0; j<n; ++j)
                    d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
        return d;
    }

    private static int getDist(int i, int j) {
        int o1 = HallManager.getHallOnline(i);
        if(o1 == -1)
            o1 = 0;
        int o2 = HallManager.getHallOnline(j);
        if(o2 == -1)
            o2 = 0;
        return o1 + o2 + 2;
    }

}
