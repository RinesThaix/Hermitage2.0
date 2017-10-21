package mem.kitek.android.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import lombok.experimental.Delegate;
import mem.kitek.R;

/**
 * Created by cat on 10/20/17.
 */

@EViewGroup(R.layout.img_card_view)
public class ImgCardView extends CardView {
    @ViewById @Delegate(excludes = TextView.class)
    ImageView image;
    @ViewById @Delegate(excludes = ImageView.class)
    TextView text;

    public ImgCardView(Context context) {
        super(context);
    }

    public ImgCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
