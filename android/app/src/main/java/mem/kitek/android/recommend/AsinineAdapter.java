package mem.kitek.android.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import mem.kitek.R;
import mem.kitek.android.meta.scope.FragmentScope;
import rx.Subscription;
import rx.subjects.BehaviorSubject;

/**
 * Created by cat on 10/20/17.
 */

@FragmentScope
public class AsinineAdapter extends RecyclerView.Adapter<AsinineAdapter.VH> {
    private final @Getter
    Context context;
    private final List<CompositeImage> imageList;
    private int fictionalSize;
    private final BehaviorSubject<Integer> sizeSubject;
    private final RecommendFragmentPresenter presenter;
    private @Setter
    @Getter TextView textViewRef;

    @Inject
    public AsinineAdapter(Context context, List<CompositeImage> imageList, RecommendFragmentPresenter presenter) {
        this.context = context;
        this.imageList = imageList;
        fictionalSize = imageList.size();
        sizeSubject = BehaviorSubject.create(imageList.size());
        this.presenter = presenter;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(getContext()).inflate(R.layout.img_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindTo(imageList.get(position), position, sizeSubject);
    }

    @Override
    public int getItemCount() {
        return fictionalSize;
    }

    public void onRemove(int adapterPosition, int resolution) {
        if (adapterPosition == fictionalSize - 1) {
            imageList.get(adapterPosition).resoulution = resolution;
            fictionalSize--;
            sizeSubject.onNext(fictionalSize);
            notifyItemRemoved(adapterPosition);
        }

        if (fictionalSize == 0) {
            presenter.resolveLikes(imageList);
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView view;
        Subscription subscription;
        public VH(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.image);
        }

        void bindTo(CompositeImage typeMe, int position, BehaviorSubject<Integer> sizeSubject) {
            subscription = sizeSubject.subscribe(it -> {
                if (it - position < 4) {
                    Picasso.with(getContext())
                            .load(typeMe.srcUrl)
                            .into(view);
                }

                if (it - position == 1) {
                    if (textViewRef != null) {
                        textViewRef.setText(typeMe.descritpion);
                    }

                    if (subscription != null && !subscription.isUnsubscribed())
                        subscription.unsubscribe();
                }
            });
        }
    }
}
