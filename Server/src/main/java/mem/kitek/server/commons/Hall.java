package mem.kitek.server.commons;

/**
 * Created by RINES on 21.10.17.
 */
public class Hall {

    private final int id;
    private final String name;
    private final HallCategory category;
    private final int floor;

    public Hall(int id, String name, HallCategory category, int floor) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.floor = floor;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public HallCategory getCategory() {
        return this.category;
    }

    public int getFloor() {
        return this.floor;
    }

}
