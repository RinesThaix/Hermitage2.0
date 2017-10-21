package mem.kitek.android;

import android.app.Application;

import autodagger.AutoComponent;
import autodagger.AutoExpose;
import autodagger.AutoInjector;
import dagger.Module;
import lombok.Getter;

/**
 * Created by cat on 10/20/17.
 */

@AutoComponent
@AutoExpose(MemeApplication.Exposer.class)
@AutoInjector
public class MemeApplication extends Application {
    private @Getter MemeApplicationComponent component;

    @Override
    public void onCreate() {
        component = DaggerMemeApplicationComponent.create();
        super.onCreate();
    }

    public MemeApplicationComponent getAppComponent() {
        return component;
    }

    public class Exposer {
        public MemeApplication application() {
            return MemeApplication.this;
        }
    }
}
