package mem.kitek.android.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;

import lombok.Getter;
import lombok.Setter;
import mem.kitek.R;

/**
 * Created by cat on 10/21/17.
 */

@EView
public class DatPin extends ImageView {
    private @Setter
    @Getter
    int hallId;
    public DatPin(Context context) {
        super(context);
    }

    public DatPin(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DatPin(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void initPin() {
        setImageResource(R.drawable.comp_pin);
        setImageLevel(2500);
    }

    public void setPeopleCount(int peopleCount) {
        setImageLevel(10000 * peopleCount / 12 );
        Log.d("MEMES", "setPeopleCount: we " + peopleCount + " at " + hallId + " bois");
    }
}
