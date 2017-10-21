package mem.kitek.server.commons;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mem.kitek.server.Bootstrap;
import mem.kitek.server.HallManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by RINES on 21.10.17.
 */
public class Building {

    private final int id;

    private final String name;

    private final Map<Integer, Integer> floorPeopleLimits;

    private final LoadingCache<Integer, Integer> peopleOnFloors = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS).concurrencyLevel(4).build(new CacheLoader<Integer, Integer>() {
                @Override
                public Integer load(Integer floor) {
                    return HallManager.calculatePeopleOnFloor(Building.this, floor);
                }
            });

    public Building(int id, String name) {
        this.id = id;
        this.name = name;
        this.floorPeopleLimits = new HashMap<>();
        try(ResultSet set = Bootstrap.getDatabase().query("SELECT * FROM people_limits WHERE building_id=%d", id)) {
            while(set.next())
                this.floorPeopleLimits.put(set.getInt(2), set.getInt(3));
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPeopleLimit(int floor) {
        return this.floorPeopleLimits.get(floor);
    }

    public int getPeopleOnFloor(int floor) {
        return this.peopleOnFloors.getUnchecked(floor);
    }

}
