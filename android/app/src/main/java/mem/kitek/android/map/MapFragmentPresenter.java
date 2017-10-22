package mem.kitek.android.map;

import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozix.tileview.widgets.ZoomPanLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import lombok.SneakyThrows;
import lombok.val;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.data.Node;
import mem.kitek.android.meta.BasePresenter;
import mem.kitek.android.meta.scope.FragmentScope;
import mem.kitek.android.recommend.CompositeImage;
import mem.kitek.android.service.CacheDump;
import mem.kitek.android.service.KiteqAPI;
import mem.kitek.android.service.ServiceAPI;
import mem.kitek.android.view.DatPin;
import mem.kitek.android.view.DatPin_;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * Created by cat on 10/21/17.
 */
@FragmentScope
class MapFragmentPresenter extends BasePresenter<MapFragment> {
    private static final String TAG = "MapP";
    public static final int MAP_WIDTH = 2560;
    public static final int MAP_HEIGHT = 1280;
    private static final double EPS = 3;
    private final ObjectMapper mapper;
    private final ServiceAPI api;
    private final CacheDump dump;
    private List<Node.NodeInfo> nodes;
    private final List<ApiData.HallInfo> halls = new ArrayList<>();
    private final List<DatPin> pins = new ArrayList<>();
    private int loadingDone = 0;

    @Inject
    public MapFragmentPresenter(MapFragment view, ObjectMapper mapper, ServiceAPI api, CacheDump dump) {
        super(view);
        this.mapper = mapper;
        this.api = api;
        this.dump = dump;
    }

    public void setupMap() {
        val map = getView().map;
        val container = getView().getContainer();
        assert container != null;
        map.setSoundEffectsEnabled(false);
        map.setSize(MAP_WIDTH, MAP_HEIGHT);
        map.addDetailLevel(1f, "maps/floor_1/tile-%d_%d.png", 256, 256);
        map.setScaleLimits(0, 3);
        map.addZoomPanListener(new ZoomPanLayout.ZoomPanListener() {
            @Override
            public void onPanBegin(int x, int y, Origination origin) {

            }

            @Override
            public void onPanUpdate(int x, int y, Origination origin) {

            }

            @Override
            public void onPanEnd(int x, int y, Origination origin) {

            }

            @Override
            public void onZoomBegin(float scale, Origination origin) {

            }

            @Override
            public void onZoomUpdate(float scale, Origination origin) {

            }

            @Override
            public void onZoomEnd(float scale, Origination origin) {
                pinVisiblity(resolvePinVisibility());
            }
        });

        setupNodes(this::loadTotal);
    }

    private int resolvePinVisibility() {
        if (getView().map.getScale() >= 1.6) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    private void pinVisiblity(int gone) {
        for (val p : pins) {
            p.setVisibility(gone);
        }
    }

    private void setupNodes(Runnable cont) {
        Observable.just("floor_1_nodes.json")
                .map(this::unsafeOpen)
                .map(this::unsafeNodeMeta)
                .map(Node.NodeGraph::getNodes)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(it -> nodes = it, e -> {}, () -> {
                    Log.d(TAG, "setupNodes: nodes loaded or not");
                    loadingDone |= 1;

                    cont.run();
                });
    }

    private void maybePeriodic() {
        if (loadingDone == 3) {
            initPeriodic();
        }
    }

    private void placeMarkerCentered(View v, int xcoord, int ycoord) {
        getView().map.addMarker(v, xcoord, ycoord, -0.5f, -0.5f);
    }

    private void placePin(View v, int xcoord, int ycoord) {
        getView().map.addMarker(v, xcoord, ycoord, -0.5f, -1f);
    }

    @SneakyThrows
    private InputStream unsafeOpen(String path) {
        return getView().getContext().getAssets().open("maps/floor_1_nodes_norm.json");
    }

    @SneakyThrows
    private Node.NodeGraph unsafeNodeMeta(InputStream s) {
        return mapper.readValue(s, Node.NodeGraph.class);
    }


    private void initPeriodic() {
        Observable<Long> interval = Observable.interval(15, TimeUnit.SECONDS);
        val si = Observable.just(-1l).delay(1, TimeUnit.SECONDS).concatWith(interval)
                .doOnEach(notification -> {
                    batchLoad();
                }).subscribe();
        register(si);
    }

    private void loadTotal() {
        val s = Observable.from(nodes)
                .filter(it -> it.hall_id != 0)
                .doOnNext(it -> {
                    Log.d(TAG, "loadTotal: hall collected : " + it.hall_id);
                })
                .flatMap(it -> dump.getHallInfo(it.hall_id))
                .filter(it -> it.getBuilding() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(hall -> {
                    halls.add(hall);
                    patchHall(hall);
                }, Exceptions::propagate, () -> {
                    Log.d(TAG, "loadTotal: COMPLEAT");
                    loadingDone |= 2;

                    maybePeriodic();
                    pinVisiblity(resolvePinVisibility());
                });
        register(s);
    }

    private void patchHall(ApiData.HallInfo hall) {
        DatPin pin = null;
        for (val p : pins) {
            if (p.getHallId() == hall.getHall_id()) {
                pin = p;
            }
        }

        if (pin == null) {
            val node = locate(hall);
            if (node != null) {
                pin = DatPin_.build(getView().getContext());
                pins.add(pin);
                placePin(pin, node.x, node.y);
            }
        }

        if (pin != null) {
            pin.setPeopleCount(hall.getPeople());
            pin.setVisibility(resolvePinVisibility());
        }
    }

    private void batchLoad() {
        StringBuilder str = new StringBuilder();
        for (val h : halls) {
            str.append(h.getHall_id()).append(',');
        }

        str.deleteCharAt(str.length() - 1); // last comma

        val s = api.getBatchPop(str.toString())
                .map(KiteqAPI::unwrap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(pp -> {
                    val it = pp.people.iterator();
                    for (val h : halls) {
                        h.setPeople(it.next());
                        patchHall(h);
                    }
                });
        register(s);
    }

    private Node.NodeInfo locate(ApiData.HallInfo hall) {
        for (val i : nodes) {
            if (i.getHall_id() == hall.getHall_id()) {
                return i;
            }
        }

        return null;
    }

    void handlePrefs(List<CompositeImage> images) {

    }
}
