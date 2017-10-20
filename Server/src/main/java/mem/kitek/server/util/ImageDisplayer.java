package mem.kitek.server.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by RINES on 20.10.17.
 */
public class ImageDisplayer {

    public static void init() {

    }

    public static void display(Path imagePath) throws Exception {
        File image = imagePath.toFile();
        File copy = new File(image.getParentFile(), System.currentTimeMillis() + ".png");
        Files.copy(imagePath, copy.toPath());
    }

}
