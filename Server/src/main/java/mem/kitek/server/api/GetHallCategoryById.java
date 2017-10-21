package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.HallCategory;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 21.10.17.
 */
public class GetHallCategoryById extends ApiMethod {

    public GetHallCategoryById() {
        super("getHallCategoryByID", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        int id = i(params, "category_id");
        HallCategory category = HallManager.getHallCategory(id);
        if(category == null)
            return ApiError.UNKNOWN_HALL_CATEGORY.toJson(id);
        JSONObject json = new JSONObject();
        json.put("category_id", id);
        json.put("category_name", category.getName());
        json.put("category_images", category.getImages());
        return json.toJSONString();
    }

}
