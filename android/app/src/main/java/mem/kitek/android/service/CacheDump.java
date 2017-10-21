package mem.kitek.android.service;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import autodagger.AutoExpose;
import lombok.Setter;
import lombok.val;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.meta.scope.AppScope;
import rx.Observable;
import rx.exceptions.Exceptions;

/**
 * Created by cat on 10/22/17.
 */

//@AppScope
public class CacheDump {
    private final ServiceAPI api;
    private @Setter
    ConcurrentHashMap<Integer, ApiData.HallInfo> halls;
    @Inject
    public CacheDump(ServiceAPI api) {
        this.api = api;
    }

    public Observable<ApiData.HallInfo> getHallInfo(int id) {
//        if (halls.get(id) == null) {
//            val it = api.getHallInfo(id).doOnNext(arg -> {
//                try {
//                    halls.put(id, KiteqAPI.unwrap(arg));
//                } catch (Exception ignored) {}
//            });
//            return it;

        return null;
    }
}
