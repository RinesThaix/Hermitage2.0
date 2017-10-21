package mem.kitek.android;

import android.app.Application;

import autodagger.AutoComponent;
import autodagger.AutoExpose;
import autodagger.AutoInjector;
import dagger.Module;
import lombok.Getter;
import mem.kitek.android.service.KiteqAPI;

/**
 * Created by cat on 10/20/17.
 */

@AutoComponent(modules = KiteqAPI.class)
@AutoExpose(MemeApplication.Exposer.class)
@AutoInjector
public class MemeApplication extends Application {
    private @Getter MemeApplicationComponent component;

    @Override
    public void onCreate() {
        component = DaggerMemeApplicationComponent
                .builder()
                .kiteqAPI(new KiteqAPI())
                .build();
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
