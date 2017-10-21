package mem.kitek.android.recommend;

import java.util.List;

import javax.inject.Inject;

import lombok.val;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.meta.BasePresenter;
import mem.kitek.android.meta.scope.FragmentScope;
import mem.kitek.android.service.KiteqAPI;
import mem.kitek.android.service.ServiceAPI;

/**
 * Created by cat on 10/22/17.
 */
@FragmentScope
class RecommendFragmentPresenter extends BasePresenter<RecommendFragment> {
    private static final int AMOUNT = 30;
    private final @Inject
    ServiceAPI api;
    private List<ApiData.Picture> curPictureList;


    @Inject
    public RecommendFragmentPresenter(RecommendFragment view, ServiceAPI api) {
        super(view);
        this.api = api;
    }

    public void requestImages() {
        val s = api.getPictureInfo(AMOUNT)
                .map(KiteqAPI::unwrap)
                .map(ApiData.PictureData::getPictures)
                .subscribe(list -> {
                    curPictureList = list;
                    
                })
    }
}
