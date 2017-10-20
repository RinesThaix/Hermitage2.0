package mem.kitek.server.util;

import java.nio.file.Path;

/**
 * Created by RINES on 20.10.17.
 */
public interface IPeopleCalculus {

    ImageNeuralParsingResult parse(Path path);

    void shutdown();

}
