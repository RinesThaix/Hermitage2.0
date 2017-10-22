package mem.kitek.android.service;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import autodagger.AutoExpose;
import dagger.Module;
import lombok.Setter;
import lombok.val;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.meta.scope.AppScope;
import rx.Observable;
import rx.exceptions.Exceptions;

/**
 * Created by cat on 10/22/17.
 */

@AppScope
@AutoExpose(MemeApplication.class)
public class CacheDump {
    private final ServiceAPI api;
    private @Setter
    ConcurrentHashMap<Integer, ApiData.HallInfo> halls;
    @Inject
    public CacheDump(ServiceAPI api) {
        this.api = api;
    }

    public Observable<ApiData.HallInfo> getHallInfo(int id) {
        if (halls.get(id) == null) {
            return api.getHallInfo(id).map(arg -> {
                ApiData.HallInfo unwrap = KiteqAPI.unwrap(arg);
                halls.put(id, unwrap);
                return unwrap;
            });
        }

        return Observable.just(halls.get(id));
    }
}
