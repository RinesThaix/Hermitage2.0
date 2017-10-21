package mem.kitek.android.recommend;

import android.support.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by cat on 10/22/17.
 */

@Value
@AllArgsConstructor
class CompositeImage {
    public String srcUrl;
    public @Nullable String descritpion;
    public int category;
    public int incatId;
}
