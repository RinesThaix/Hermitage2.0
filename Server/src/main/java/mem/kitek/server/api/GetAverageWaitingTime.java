package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.commons.ApiMethod;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 22.10.17.
 */
public class GetAverageWaitingTime extends ApiMethod {

    public GetAverageWaitingTime() {
        super("getAverageWaitingTime", Lists.newArrayList(), Lists.newArrayList());
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        JSONObject json = new JSONObject();
        json.put("minutes", 0);
        return json.toJSONString();
    }
}
