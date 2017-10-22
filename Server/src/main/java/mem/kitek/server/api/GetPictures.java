package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.Hall;
import mem.kitek.server.commons.HallCategory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by RINES on 21.10.17.
 */
public class GetPictures extends ApiMethod {

    public GetPictures() {
        super("getPictures", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        int amount = i(params, "amount");
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        List<Integer> ids = new ArrayList<>();
        Map<Integer, List<Integer>> categories = new HashMap<>();
        for(int i = 0; i < HallManager.getHallsCategoriesSize(); ++i) {
            HallCategory category = HallManager.getHallCategory(i);
            Collection<Hall> halls = HallManager.getHallsByCategory(category);
            if(halls == null || halls.iterator().next().getFloor() != 1)
                continue;
            if(category.getImages() > 0) {
                List<Integer> list = new ArrayList<>();
                for(int j = 0; j < category.getImages(); ++j) {
                    ids.add(i);
                    list.add(j);
                }
                categories.put(i, list);
            }
        }
        Collections.shuffle(ids);
        for(int i = 0; i < Math.min(ids.size(), amount); ++i) {
            JSONObject picture = new JSONObject();
            HallCategory category = HallManager.getHallCategory(ids.get(i));
            picture.put("cid", category.getId());
            List<Integer> list = categories.get(category.getId());
            int lid = ThreadLocalRandom.current().nextInt(list.size());
            picture.put("pid", list.remove(lid));
            if(list.isEmpty())
                categories.remove(category.getId());
            array.add(picture);
        }

        json.put("amount", array.size());
        json.put("pictures", array);
        return json.toJSONString();
    }

}
