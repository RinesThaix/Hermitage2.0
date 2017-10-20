package mem.kitek.server.api;

import com.google.common.collect.Lists;
import mem.kitek.server.commons.ApiError;
import mem.kitek.server.commons.ApiMethod;
import mem.kitek.server.commons.ApiMethodArgument;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by RINES on 20.10.17.
 */
public class TestMethod extends ApiMethod {

    public TestMethod() {
        super("testmethod", Lists.newArrayList(
                new ApiMethodArgument("first", int.class),
                new ApiMethodArgument("second", int.class)
        ), Lists.newArrayList(
                ApiError.ARGUMENT_NOT_SPECIFIED,
                ApiError.ILLEGAL_ARGUMENT_FORMAT
        ));
    }

    @Override
    public String process(Map<String, String> params) throws Exception {
        JSONObject json = new JSONObject();
        int first = i(params, "first"), second = i(params, "second");
        json.put("first", first);
        json.put("second", second);
        json.put("result", first + second);
        return json.toJSONString();
    }

}
