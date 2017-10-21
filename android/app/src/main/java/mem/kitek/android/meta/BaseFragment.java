package mem.kitek.android.meta;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import lombok.Setter;
import lombok.val;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;

/**
 * Created by cat on 10/21/17.
 */

public abstract class BaseFragment extends Fragment {
    private boolean initOnce = false;

    @Override
    public void onAttach(Context context) {
        if (!initOnce) {
            val component = ((MemeApplication) context.getApplicationContext()).getAppComponent();
            performInject(component);
            initOnce = true;
        }

        super.onAttach(context);
    }

    protected abstract void performInject(MemeApplicationComponent component);
}
