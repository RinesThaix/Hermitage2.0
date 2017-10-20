package mem.kitek.server;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by RINES on 20.10.17.
 */
public class HallManager {

    private final static Map<Integer, Integer> CAMS_TO_HALLS_RELATIVE = new HashMap<>();
    private final static Map<Integer, Set<Integer>> HALLS_TO_CAMS_RELATIVE = new HashMap<>();
    private final static Cache<Integer, Integer> CAMS_ONLINE = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES).concurrencyLevel(4).build();
    private final static LoadingCache<Integer, Integer> HALLS_ONLINE = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES).concurrencyLevel(4).build(new CacheLoader<Integer, Integer>() {
                @Override
                public Integer load(Integer hallId) {
                    return HALLS_TO_CAMS_RELATIVE.get(hallId).stream().mapToInt(cam -> {
                        Integer online = CAMS_ONLINE.getIfPresent(cam);
                        return online == null ? 0 : online;
                    }).sum();
                }
            });

    public static void init() {
        initCamsToHallsRelative();
    }

    public static void changeOnline(int camId, int online) {
        CAMS_ONLINE.put(camId, online);
        HALLS_ONLINE.refresh(CAMS_TO_HALLS_RELATIVE.get(camId));
    }

    public static int getHallOnline(int hallId) {
        if(!HALLS_TO_CAMS_RELATIVE.containsKey(hallId))
            return -1;
        return HALLS_ONLINE.getUnchecked(hallId);
    }

    private static void initCamsToHallsRelative() {
        registerCam(1, 1);
        registerCam(2, 2);
    }

    private static void registerCam(int camId, int hallId) {
        CAMS_TO_HALLS_RELATIVE.put(camId, hallId);
        Set<Integer> backRelative = HALLS_TO_CAMS_RELATIVE.get(hallId);
        if(backRelative == null) {
            backRelative = new HashSet<>();
            HALLS_TO_CAMS_RELATIVE.put(hallId, backRelative);
        }
        backRelative.add(camId);
    }

    private static int getHallIdByCamId(int camId) {
        return CAMS_TO_HALLS_RELATIVE.get(camId);
    }

}
