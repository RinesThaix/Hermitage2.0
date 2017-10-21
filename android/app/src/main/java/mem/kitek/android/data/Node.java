package mem.kitek.android.data;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by cat on 10/21/17.
 */

public class Node {
    public static class NodeGraph {
        public int count = 0;
        public @Getter
        ArrayList<NodeInfo> nodes = new ArrayList<>();
    }

    @Data
    public static class NodeInfo {
        public @Nullable @Setter
        @Getter ApiData.HallInfo hall_info;
        public @Nullable int hall_id;
        public int id;
        public int x;
        public int y;
        public @Getter ArrayList<Integer> edges = new ArrayList<>();
    }

}
