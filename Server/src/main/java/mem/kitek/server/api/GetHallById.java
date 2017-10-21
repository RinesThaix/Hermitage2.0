package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.Hall;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 21.10.17.
 */
public class GetHallById extends ApiMethod {

    public GetHallById() {
        super("getHallByID", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        int id = i(params, "hall_id");
        Hall hall = HallManager.getHall(id);
        if(hall == null)
            return ApiError.UNKNOWN_HALL.toJson(id);
        JSONObject json = new JSONObject();
        json.put("hall_id", id);
        json.put("hall_name", hall.getName());
        json.put("hall_floor", hall.getFloor());
        JSONObject category = new JSONObject();
        category.put("id", hall.getCategory().getId());
        category.put("name", hall.getCategory().getName());
        category.put("images", hall.getCategory().getImages());
        json.put("category", category);
        return json.toJSONString();
    }

}
