package mem.kitek.android.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import lombok.experimental.Delegate;
import mem.kitek.R;

/**
 * Created by cat on 10/22/17.
 */

@EViewGroup(R.layout.overlay_load_view)
public class LoadingOverlayer extends FrameLayout {
    @ViewById
    TextView text;

    String defer;

    public LoadingOverlayer(@NonNull Context context) {
        super(context);
    }

    public LoadingOverlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingOverlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingOverlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setText(String nt) {
        if (text == null) {
            defer = nt;
        } else {
            text.setText(nt);
        }
    }

    @AfterViews
    void redefer() {
        if (defer != null)
            text.setText(defer);
    }
}
