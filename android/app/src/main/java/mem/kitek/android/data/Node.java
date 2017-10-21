package mem.kitek.android.data;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Created by cat on 10/21/17.
 */

public class Node {
    public static class NodeMeta {
        public int count = 0;
        public @Getter
        ArrayList<NodeInfo> nodes = new ArrayList<>();
    }

    public static class NodeInfo {
        public int id;
        public int x;
        public int y;
        public @Getter ArrayList<Integer> edges = new ArrayList<>();
    }
}
