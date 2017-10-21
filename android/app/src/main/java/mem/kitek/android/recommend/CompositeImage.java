package mem.kitek.android.recommend;

import android.support.annotation.Nullable;

import lombok.Value;

/**
 * Created by cat on 10/22/17.
 */

@Value
class CompositeImage {
    public String srcUrl;
    public int category;
    public int incatId;
    public @Nullable String descritpion;
}
