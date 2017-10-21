package mem.kitek.android.map;

import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qozix.tileview.paths.CompositePathView;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import lombok.SneakyThrows;
import lombok.val;
import mem.kitek.android.data.Node;
import mem.kitek.android.meta.BasePresenter;
import mem.kitek.android.meta.scope.FragmentScope;
import mem.kitek.android.view.FuknCircle_;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by cat on 10/21/17.
 */
@FragmentScope
class MapFragmentPresenter extends BasePresenter<MapFragment> {
    private static final String TAG = "MapP";
    public static final int MAP_WIDTH = 2560;
    public static final int MAP_HEIGHT = 1280;
    private static final double EPS = 3;
    private final ObjectMapper mapper = new ObjectMapper();
    private ArrayList<Node.NodeInfo> nodes;

    @Inject
    public MapFragmentPresenter(MapFragment view) {
        super(view);
    }

    public void setupMap() {
        val map = getView().map;
        val container = getView().getContainer();
        assert container != null;
        map.setSoundEffectsEnabled(false);
        map.setSize(MAP_WIDTH, MAP_HEIGHT);
        map.addDetailLevel(1f, "maps/floor_1/tile-%d_%d.png", 256, 256);
        map.setScaleLimits(0, 3);
    }

    public void setupDebugPins() {
        val map = getView().map;
        placeMarkerCentered(FuknCircle_.build(getView().getContext()), 140, 265);
        placeMarkerCentered(FuknCircle_.build(getView().getContext()), MAP_WIDTH - 500, MAP_HEIGHT - 260);
    }

    public void setupNodes() {
        Observable.just("floor_1_nodes.json")
                .map(this::unsafeOpen)
                .map(this::unsafeNodeMeta)
                .map(Node.NodeMeta::getNodes)
                .doOnNext(it -> nodes = it)
                .flatMap(Observable::from)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(node -> {
                    val paint = new Paint();
                    paint.setStyle( Paint.Style.STROKE );
                    paint.setAntiAlias(false);
                    paint.setARGB(0xff, 0x00, 0, 0);
                    paint.setStrokeWidth(3);
                    val v = new TextView(getView().getContext());
                    v.setText(String.valueOf(node.id));
                    v.setTextColor(0xffff0000);
//                    val v = FuknCircle_.build(getView().getContext());
                    placeMarkerCentered(v, node.x, node.y);
                    for (val t : node.edges) {
                        if (t > node.id) {
                            val tn = nodes.get(t);

                            val dp = new CompositePathView.DrawablePath();
                            val p = new Path();
                            p.moveTo(node.x, node.y);
                            Log.d(TAG, "setupNodes: line from " + node.x + " " + node.y);
                            p.lineTo(tn.x, tn.y);
                            Log.d(TAG, "setupNodes: line to " + tn.x + " " + tn.y);
                            Log.d(TAG, "setupNodes: ");

                            dp.path = p;
                            dp.paint = paint;

                            Log.d(TAG, "setupNodes: drawing a path! " + node.id + " " + t);

                            getView().map.drawPath(dp);
                        }
                    }
                })
                .subscribe();
    }

    private void placeMarkerCentered(View v, int xcoord, int ycoord) {
        getView().map.addMarker(v, xcoord, ycoord, -0.5f, -0.5f);
    }

    @SneakyThrows
    private InputStream unsafeOpen(String path) {
        return getView().getContext().getAssets().open("maps/floor_1_nodes.json");
    }

    @SneakyThrows
    private Node.NodeMeta unsafeNodeMeta(InputStream s) {
        return mapper.readValue(s, Node.NodeMeta.class);
    }
}
