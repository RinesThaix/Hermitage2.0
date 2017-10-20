package mem.kitek.server.commons;

/**
 * Created by RINES on 20.10.17.
 */
public class ApiMethodArgument {

    private final String name;
    private final Class<?> type;

    public ApiMethodArgument(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.type;
    }

}
