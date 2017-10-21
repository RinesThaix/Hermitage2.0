package mem.kitek.android.map;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qozix.tileview.TileView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoExpose;
import autodagger.AutoInjector;
import dagger.Module;
import dagger.Provides;
import lombok.Getter;
import lombok.val;
import mem.kitek.R;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;
import mem.kitek.android.main.MainActivity;
import mem.kitek.android.meta.BaseFragment;
import mem.kitek.android.meta.scope.FragmentScope;

/**
 * Created by cat on 10/21/17.
 */

@FragmentScope
@AutoInjector
@AutoComponent(dependencies = {MemeApplication.class}, modules = MapFragment.Exposer.class)
@EFragment(R.layout.map_fragment)
public class MapFragment extends BaseFragment {
    public static final String TAG = "Map";
    @ViewById @Getter
    TileView map;
    @Nullable @Getter
    private ViewGroup container;
    @Inject
    MapFragmentPresenter presenter;

    @Override
    protected void performInject(MemeApplicationComponent component) {
        Log.d(TAG, "performInject: yey!");

        DaggerMapFragmentComponent
                .builder()
                .memeApplicationComponent(component)
                .exposer(new Exposer())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        Log.d(TAG, "onCreateView: creating it yo");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: STARTING!");
        super.onStart();
    }

    @Module
    public class Exposer {
        @Provides
        MapFragment provideIt() {
            return MapFragment.this;
        }
    }

    @AfterViews
    void init() {
        Log.d(TAG, "init: happening!");
        presenter.setupMap();
        presenter.setupNodes();
    }


}
