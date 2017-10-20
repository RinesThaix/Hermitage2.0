package mem.kitek.server.util;

import java.nio.file.Path;

/**
 * Created by RINES on 20.10.17.
 */
public class ImageNeuralParsingResult {

    private final int people;
    private final Path pathToHighlightedImage;

    public ImageNeuralParsingResult(int people, Path pathToHighlightedImage) {
        this.people = people;
        this.pathToHighlightedImage = pathToHighlightedImage;
    }

    public int getPeople() {
        return this.people;
    }

    public Path getPathToHighlightedImage() {
        return this.pathToHighlightedImage;
    }

}
