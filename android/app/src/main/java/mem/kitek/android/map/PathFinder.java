package mem.kitek.android.map;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import mem.kitek.android.data.ApiData.HallInfo;
import mem.kitek.android.data.Node.NodeInfo;

public class PathFinder {
    private List<NodeInfo> nodes;

    public PathFinder(List<NodeInfo> nodes) {
        this.nodes = nodes;
    }

    private List<NodeInfo> findCompositePath(NodeInfo start, NodeInfo end) {
        Queue<NodeInfo> queue = new ArrayDeque<>();
        queue.add(start);
        Set<NodeInfo> used = new HashSet<>();
        Map<NodeInfo, NodeInfo> par = new HashMap<>();
        while (!queue.isEmpty()) {
            NodeInfo curr = queue.poll();
            if (curr.getId() == end.getId()) {
                break;
            }
            used.add(curr);
            for (Integer nextInd : curr.getEdges()) {
                NodeInfo next = nodes.get(nextInd);
                if (!used.contains(next)) {
                    queue.add(next);
                    par.put(next, curr);
                }
            }
        }
        List<NodeInfo> path = new ArrayList<>();
        NodeInfo curr = end;
        while (curr != null && curr.getId() != start.getId()) { // actually never null I hope
            curr = par.get(end);
        }
        return path;
    }

    public List<NodeInfo> findCompositePath(List<NodeInfo> allNodes, List<HallInfo> path) {
        Map<Integer, NodeInfo> hallToNode = new HashMap<>();
        for (NodeInfo node : allNodes) {
            if (node.hall_id != 0) {
                hallToNode.put(node.hall_id, node);
            }
        }

        List<NodeInfo> nodePath = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            NodeInfo start = hallToNode.get(path.get(i).getHall_id()); // TODO which one should I pick???
            NodeInfo end = hallToNode.get(path.get(i + 1).getHall_id());
            nodePath.addAll(findCompositePath(start, end));
        }
        return nodePath;
    }
}
