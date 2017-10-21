package mem.kitek.android.meta;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import lombok.Getter;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;

/**
 * Created by cat on 10/20/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private @Getter
    MemeApplicationComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (component == null) {
            component = ((MemeApplication) getApplication()).getAppComponent();
            this.performInject(component);
        }
        super.onCreate(savedInstanceState);
    }

    protected abstract void performInject(MemeApplicationComponent applicationComponent);
}
