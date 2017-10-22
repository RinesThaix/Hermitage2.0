package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.Hall;
import mem.kitek.server.commons.HallCategory;
import mem.kitek.server.map.HermitageMap;
import mem.kitek.server.path_finder.PathCalculus;
import mem.kitek.server.util.IRecommendation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by RINES on 21.10.17.
 */
public class TinderFinished extends ApiMethod {

    private final IRecommendation recommendation = new IRecommendationImpl();

    public TinderFinished() {
        super("tinderFinished", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        String probabilities = s(params, "probabilities");
        String[] args = probabilities.split("\\-");
        Map<HallCategory, Float> probs = new HashMap<>();
        for(int i = 0; i < args.length; i += 2) {
            int id = Integer.parseInt(args[i]);
            int probability = Integer.parseInt(args[i + 1]);
            probs.put(HallManager.getHallCategory(id), probability / 100F);
        }
        Set<Hall> result = this.recommendation.getNecessaryHalls(probs);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
//        Path path = new Annealer(HermitageMap.getGraph(), result).run();
        List<Integer> path = PathCalculus.getPath(HermitageMap.getGraph(), 11, 0, result.stream().map(Hall::getId).collect(Collectors.toSet()));
        array.addAll(path);
        json.put("halls", array);
        return json.toJSONString();
    }
}
