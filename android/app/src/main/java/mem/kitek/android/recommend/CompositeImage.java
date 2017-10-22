package mem.kitek.android.recommend;

import android.support.annotation.Nullable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by cat on 10/22/17.
 */

@Data
public class CompositeImage implements Serializable {
    public static final int LIKE = 1;
    public static final int DISLIKE = -1;

    public CompositeImage(String srcUrl, String descritpion, int category, int incatId) {
        this.srcUrl = srcUrl;
        this.descritpion = descritpion;
        this.category = category;
        this.incatId = incatId;
    }

    public String srcUrl;
    public @Nullable String descritpion;
    public int category;
    public int incatId;
    public int resoulution = 0;

    @Data
    @Parcel
    public static class Holder {

        List<CompositeImage> imageList;

        @ParcelConstructor
        public Holder(List<CompositeImage> imageList) {
            this.imageList = imageList;
        }
    }
}
