package mem.kitek.android.recommend;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Random;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import dagger.Module;
import dagger.Provides;
import lombok.Getter;
import lombok.val;
import mem.kitek.R;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;
import mem.kitek.android.meta.BaseActivity;
import mem.kitek.android.meta.scope.ActivityScope;

/**
 * Created by cat on 10/20/17.
 */

@EActivity(R.layout.recommend_activity)
@ActivityScope
@AutoComponent(dependencies = MemeApplication.class, modules = RecommendActivity.Exposer.class)
@AutoInjector
public class RecommendActivity extends BaseActivity {
    @Inject
    RecommendActivityPresenter presenter;

    @ViewById
    RecyclerView recycler;
    private @Getter AsinineAdapter adapter;
    private final Random random = new Random();

    @Override
    protected void performInject(MemeApplicationComponent applicationComponent) {
        DaggerRecommendActivityComponent
                .builder()
                .memeApplicationComponent(applicationComponent)
                .exposer(new Exposer())
                .build()
                .inject(this);
    }

    @Module
    class Exposer {
        @Provides
        RecommendActivity provideIt() {
            return RecommendActivity.this;
        }
    }

    @AfterViews
    void init() {
        fuckUpRecycler();
    }

    private void fuckUpRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int anyMS = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(anyMS, anyMS);
                val i = parent.getChildAdapterPosition(view);
                if (i != 0) {
                    outRect.set(0, -view.getMeasuredHeight() + getOffset(), 0, 0);
                }
                else
                    outRect.set(0, 0, 0, 0);
            }
        });

        val ithc = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int adapterPosition = viewHolder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    AsinineAdapter adapter = getAdapter();
                    if (adapter != null)
                        adapter.onRemove(adapterPosition, (direction == ItemTouchHelper.LEFT) ? CompositeImage.DISLIKE : CompositeImage.LIKE);
                }
            }
        };

        val ith = new ItemTouchHelper(ithc);

        ith.attachToRecyclerView(recycler);
    }

    private int getOffset() {
        return 4; // inlineme
        // extractme
        // do dirty things to me
    }
}
