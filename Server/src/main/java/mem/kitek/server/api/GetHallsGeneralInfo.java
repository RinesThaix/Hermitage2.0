package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiMethod;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 21.10.17.
 */
public class GetHallsGeneralInfo extends ApiMethod {

    public GetHallsGeneralInfo() {
        super("getHallsGeneralInfo", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        JSONObject json = new JSONObject();
        json.put("categories_in_total", HallManager.getHallsCategoriesSize());
        json.put("halls_in_total", HallManager.getHallsSize());
        return json.toJSONString();
    }

}
