package mem.kitek.android.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import mem.kitek.R;

/**
 * Created by cat on 10/20/17.
 */

public class AsinineAdapter extends RecyclerView.Adapter<AsinineAdapter.VH> {
    private final @Getter
    Context context;
    private final List<CompositeImage> imageList;

    @Inject
    public AsinineAdapter(Context context, List<CompositeImage> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(getContext()).inflate(R.layout.img_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bindTo(position);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void onRemove(int adapterPosition) {
        imageList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(View itemView) {
            super(itemView);
        }

        void bindTo(Object typeMe) {

        }
    }
}
