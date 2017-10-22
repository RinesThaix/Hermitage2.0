package mem.kitek.android.main;

import android.app.job.JobWorkItem;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import dagger.Module;
import dagger.Provides;
import lombok.val;
import mem.kitek.R;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;
import mem.kitek.android.map.MapFragment_;
import mem.kitek.android.meta.BaseActivity;
import mem.kitek.android.meta.scope.ActivityScope;
import mem.kitek.android.recommend.RecommendFragment_;
import mem.kitek.android.service.KiteqAPI;
import mem.kitek.android.service.ServiceAPI;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cat on 10/20/17.
 */

@ActivityScope
@AutoComponent(modules = MainActivity.Exposer.class, dependencies = MemeApplication.class)
@AutoInjector
@EActivity(R.layout.main_activity)
public class MainActivity extends BaseActivity {
    @ViewById
    AHBottomNavigation tabbar;

    @ViewById
    FrameLayout container;

    @Inject
    ServiceAPI api;

    private int currentPosition = 0;

    @Override
    protected void performInject(MemeApplicationComponent applicationComponent) {
        DaggerMainActivityComponent
                .builder()
                .memeApplicationComponent(applicationComponent)
                .exposer(new Exposer())
                .build()
                .inject(this);
    }

    @Module
    public class Exposer {
        @Provides
        public MainActivity provideIt() {
            return MainActivity.this;
        }
    }

    @AfterViews
    void init() {
        tabbar.addItem(new AHBottomNavigationItem("Карта", R.drawable.ic_map_black_24dp));
        tabbar.addItem(new AHBottomNavigationItem("Посоветуй!", R.drawable.ic_search_black_24dp));
        tabbar.addItem(new AHBottomNavigationItem("Хм...", R.drawable.ic_more_horiz_black_24dp));
        tabbar.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        tabbar.setAccentColor(getResources().getColor(R.color.colorAccent));
        tabbar.setInactiveColor(getResources().getColor(R.color.colorPrimaryButNotSoDark));

        tabbar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (!wasSelected) {
                    if (position != 2) {
                        toFragment(position);
                    } else {
                        api.time()
                                .map(KiteqAPI::unwrap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(t ->
                                Toast.makeText(MainActivity.this, "Время ожидания: " + t.minutes + " мин.", Toast.LENGTH_SHORT).show());
                        return false;
                    }
                }


                return !wasSelected;
            }
        });

        toFragment(0);
    }

    public void toFragment(Fragment f, int position) {
        if (f == null) {
            Log.e("Main", "toFragment: to null?");
            return;
        }

        tabbar.setCurrentItem(position, false);
        val fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, f, "tag").commit();
    }

    private void toFragment(int position) {
        val fm = getSupportFragmentManager();
        val f = resolveForPosition(position);

        if (f == null) {
            Log.e("Main", "toFragment: resolver returned null?");
            return;
        }

        fm.beginTransaction().replace(R.id.container, f).commit();
    }

    private Fragment resolveForPosition(int position) {
        switch (position) {
            case 0:
                return MapFragment_.builder().build();
            case 1:
                return RecommendFragment_.builder().build();
            default:
                return null;
        }
    }
}
