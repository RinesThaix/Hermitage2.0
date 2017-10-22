package mem.kitek.android.recommend;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import dagger.Module;
import dagger.Provides;
import lombok.val;
import mem.kitek.R;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.MemeApplicationComponent;
import mem.kitek.android.meta.BaseFragment;
import mem.kitek.android.meta.scope.FragmentScope;

/**
 * Created by cat on 10/22/17.
 */


@FragmentScope
@AutoInjector
@AutoComponent(dependencies = {MemeApplication.class}, modules = RecommendFragment.Exposer.class)
@EFragment(R.layout.rec_fragment)
public class RecommendFragment extends BaseFragment {
    @ViewById
    RecyclerView recycler;
    private AsinineAdapter adapter;

    @ViewById
    TextView text;
    @Inject
    RecommendFragmentPresenter presenter;

    @Override
    protected void performInject(MemeApplicationComponent component) {
        DaggerRecommendFragmentComponent.builder()
                .memeApplicationComponent(component)
                .exposer(new Exposer())
                .build()
                .inject(this);
    }

    @Module
    public class Exposer {
        @Provides
        RecommendFragment provideIt() {
            return RecommendFragment.this;
        }
    }

    @AfterViews
    void init() {
        preInitRecycler();
        presenter.requestImages();
//        setRecycler(imageList);
    }

    public void setRecycler(List<CompositeImage> imageList) {
        adapter = new AsinineAdapter(getContext(), imageList, presenter);
        adapter.setTextViewRef(text);
        if (recycler != null)
            recycler.setAdapter(adapter);
    }

    private void preInitRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int anyMS = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(anyMS, anyMS);
                val i = parent.getChildAdapterPosition(view);
                if (i != 0) {
                    outRect.set(0, -view.getMeasuredHeight() + 3, 0, 0);
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

                    if (adapter != null)
                        adapter.onRemove(adapterPosition, (direction == ItemTouchHelper.LEFT) ? CompositeImage.DISLIKE : CompositeImage.LIKE);
                }
            }
        };

        val ith = new ItemTouchHelper(ithc);

        ith.attachToRecyclerView(recycler);
    }

}
