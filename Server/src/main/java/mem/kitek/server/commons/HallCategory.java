package mem.kitek.server.commons;

import java.io.File;

/**
 * Created by RINES on 21.10.17.
 */
public class HallCategory {

    private final int id;
    private final String name;
    private final int images;

    public HallCategory(int id, String name) {
        this.id = id;
        this.name = name;
        File images = new File("src/main/webapp/categories/" + id);
        if(!images.exists())
            images.mkdirs();
        this.images = images.listFiles().length;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Количество картинок по данной категории.
     * @return количество картинок по данной категории.
     */
    public int getImages() {
        return this.images;
    }

}
