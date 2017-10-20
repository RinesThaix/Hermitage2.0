package mem.kitek.server.api;

import mem.kitek.server.HallManager;
import mem.kitek.server.util.IPeopleCalculus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by RINES on 20.10.17.
 */
public class SendPictureMethod {

    private final static File temporary = new File("temporary");
    private final static IPeopleCalculus calculus = null;

    static {
        if(!temporary.exists())
            temporary.mkdir();
    }

    public static void process(String name, byte[] image, int camId) throws Exception {
        File tempImage = new File(temporary, name);
        tempImage.createNewFile();
        Files.write(tempImage.toPath(), image);
        int people = getPeopleOn(tempImage.toPath());
        tempImage.delete();
        HallManager.changeOnline(camId, people);
    }

    private synchronized static int getPeopleOn(Path path) {
        return calculus == null ? 0 : calculus.getPeopleOn(path);
    }

}
