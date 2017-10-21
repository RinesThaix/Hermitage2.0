package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.HallManager;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.ApiMethodArgument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by RINES on 21.10.17.
 */
public class BunchPeopleInHalls extends ApiMethod {

    public BunchPeopleInHalls() {
        super("bunchPeopleInHalls", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        String halls = s(params, "halls");
        String[] args = halls.split(",");
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        for(String arg : args)
            array.add(HallManager.getHallOnline(Integer.parseInt(arg)));
        json.put("people", array);
        return json.toJSONString();
    }
}
