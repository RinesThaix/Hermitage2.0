package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 20.10.17.
 */
public class GetPeopleInHall extends ApiMethod {

    public GetPeopleInHall() {
        super("getPeopleInHall", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        JSONObject json = new JSONObject();
        int hallId = i(params, "hall_id");
        int people = HallManager.getHallOnline(hallId);
        if(people == -1)
            return ApiError.UNKNOWN_HALL.toJson(hallId);
        json.put("hall_id", hallId);
        json.put("people", people);
        return json.toJSONString();
    }

}
