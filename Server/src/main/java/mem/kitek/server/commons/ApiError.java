package mem.kitek.server.commons;

import org.json.simple.JSONObject;

/**
 * Created by RINES on 20.10.17.
 */
public enum ApiError {
    UNKNOWN_METHOD("Method with specified name doesn't exist"),
    INTERNAL_EXCEPTION("Internal exception occurred whilst executing your request: %s"),
    ARGUMENT_NOT_SPECIFIED("We were unable to find argument %s in your request"),
    ILLEGAL_ARGUMENT_FORMAT("Some of arguments are given in illegal format"),
    UNKNOWN_HALL("Hall with given id (%d) doesn't exist"),
    UNKNOWN_HALL_CATEGORY("Hall category with given id (%d) doesn't exist");

    private final String text;

    ApiError(String text) {
        this.text = text;
    }

    public int getID() {
        return ordinal();
    }

    public String getText() {
        return this.text;
    }

    public String toJson(Object... formatParams) {
        JSONObject json = new JSONObject();
        json.put("error", true);
        json.put("error_id", getID());
        json.put("error_message", String.format(getText(), formatParams));
        return json.toJSONString();
    }

}
