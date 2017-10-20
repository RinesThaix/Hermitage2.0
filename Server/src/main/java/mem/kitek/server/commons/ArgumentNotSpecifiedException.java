package mem.kitek.server.commons;

/**
 * Created by RINES on 20.10.17.
 */
public class ArgumentNotSpecifiedException extends IllegalArgumentException {

    private final String argument;

    public ArgumentNotSpecifiedException(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return this.argument;
    }

}
