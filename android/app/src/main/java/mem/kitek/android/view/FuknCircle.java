package mem.kitek.android.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.androidannotations.annotations.EViewGroup;

import mem.kitek.R;

/**
 * Created by cat on 10/21/17.
 */

@EViewGroup(R.layout.circle_view)
public class FuknCircle extends FrameLayout {

    public FuknCircle(@NonNull Context context) {
        super(context);
    }

    public FuknCircle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FuknCircle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
