package mem.kitek.server.commons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RINES on 20.10.17.
 */
public abstract class ApiMethod {

    private final static Map<String, ApiMethod> METHODS = new HashMap<>();

    public static ApiMethod lookup(String name) {
        return METHODS.get(name.toLowerCase());
    }

    private final String name;

    private final List<ApiMethodArgument> arguments;

    private final List<ApiError> possibleErrors;

    public ApiMethod(String name, List<ApiMethodArgument> arguments, List<ApiError> possibleErrors) {
        this.name = name;
        this.arguments = arguments;
        this.possibleErrors = possibleErrors;
        METHODS.put(name.toLowerCase(), this);
    }

    public String getName() {
        return this.name;
    }

    public List<ApiMethodArgument> getArguments() {
        return this.arguments;
    }

    public List<ApiError> getPossibleErrors() {
        return this.possibleErrors;
    }

    public abstract String process(Map<String, String> params) throws Exception;

    protected int i(Map<String, String> params, String name) {
        return Integer.parseInt(get(params, name));
    }

    protected String s(Map<String, String> params, String name) {
        return get(params, name);
    }

    private String get(Map<String, String> params, String name) {
        String value = params.get(name);
        if(value == null)
            throw new ArgumentNotSpecifiedException(name);
        return value;
    }

}
