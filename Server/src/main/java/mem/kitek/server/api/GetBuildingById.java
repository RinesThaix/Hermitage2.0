package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.Building;
import mem.kitek.server.commons.HallCategory;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 21.10.17.
 */
public class GetBuildingById extends ApiMethod {

    public GetBuildingById() {
        super("getBuildingByID", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        int id = i(params, "building_id");
        Building building = HallManager.getBuilding(id);
        if(building == null)
            return ApiError.UNKNOWN_BUILDING.toJson(id);
        JSONObject json = new JSONObject();
        json.put("building_id", id);
        json.put("building_name", building.getName());
        return json.toJSONString();
    }

}
