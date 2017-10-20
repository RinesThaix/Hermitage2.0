package mem.kitek.server.api;

import mem.kitek.server.HallManager;
import mem.kitek.server.util.Darknet;
import mem.kitek.server.util.IPeopleCalculus;
import mem.kitek.server.util.ImageDisplayer;
import mem.kitek.server.util.ImageNeuralParsingResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by RINES on 20.10.17.
 */
public class SendPictureMethod {

    private final static File temporary = new File("temporary");
    private final static IPeopleCalculus calculus;

    static {
        try {
            calculus = new Darknet();
        } catch (IOException e) {
            System.err.println("blyat");
            throw new RuntimeException();
        }
        if(!temporary.exists())
            temporary.mkdir();
        ImageDisplayer.init();
    }

    public static void process(String name, byte[] image, int camId) throws Exception {
        File tempImage = new File(temporary, name);
        tempImage.createNewFile();
        Files.write(tempImage.toPath(), image);
        ImageNeuralParsingResult result = getPeopleOn(tempImage.toPath());
        if(result != null) {
            HallManager.changeOnline(camId, result.getPeople());
            ImageDisplayer.display(result.getPathToHighlightedImage());
            result.getPathToHighlightedImage().toFile().delete();
        }
        tempImage.delete();
    }

    private synchronized static ImageNeuralParsingResult getPeopleOn(Path path) {
        return calculus == null ? null : calculus.parse(path);
    }

}
