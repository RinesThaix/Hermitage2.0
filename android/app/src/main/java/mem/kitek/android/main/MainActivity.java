package mem.kitek.android.main;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import autodagger.AutoComponent;
import autodagger.AutoExpose;
import autodagger.AutoInjector;
import dagger.Module;
import dagger.Provides;
import lombok.val;
import mem.kitek.R;
import mem.kitek.android.DaggerMemeApplicationComponent;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;
import mem.kitek.android.map.MapFragment;
import mem.kitek.android.map.MapFragment_;
import mem.kitek.android.meta.BaseActivity;
import mem.kitek.android.meta.scope.ActivityScope;
import mem.kitek.android.recommend.RecommendFragment_;

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
        tabbar.addItem(new AHBottomNavigationItem("Map", R.drawable.ic_map_black_24dp));
        tabbar.addItem(new AHBottomNavigationItem("Recommend", R.drawable.ic_search_black_24dp));
        tabbar.addItem(new AHBottomNavigationItem("Memes", R.drawable.ic_more_horiz_black_24dp));

        tabbar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (!wasSelected)
                    toFragment(position);

                return !wasSelected;
            }
        });

        toFragment(0);
    }

    private void toFragment(int position) {
        val fm = getSupportFragmentManager();
        val f = resolveForPosition(position);

        if (f == null) {
            Log.e("Main", "toFragment: resolver returned null?");
            return;
        }

        fm.beginTransaction().replace(R.id.container, f, "tag").commitNowAllowingStateLoss();
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
