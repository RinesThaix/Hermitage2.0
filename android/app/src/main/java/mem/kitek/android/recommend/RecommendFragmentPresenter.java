package mem.kitek.android.recommend;

import java.util.List;

import javax.inject.Inject;

import lombok.val;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.meta.BasePresenter;
import mem.kitek.android.meta.scope.FragmentScope;
import mem.kitek.android.service.KiteqAPI;
import mem.kitek.android.service.ServiceAPI;
import rx.Observable;

/**
 * Created by cat on 10/22/17.
 */
@FragmentScope
class RecommendFragmentPresenter extends BasePresenter<RecommendFragment> {
    private static final int AMOUNT = 30;
    private final ServiceAPI api;
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
                .subscribe(this::processData, e -> {}, () -> {});
    }

    private void processData(List<ApiData.Picture> pictures) {
        Observable<ApiData.Picture> from = Observable.from(pictures);
        val o1 = from
                .map(pic -> "http://kitek.kostya.sexy/api/"
                                + "categories/"
                                + pic.cid + "/"
                                + pic.pid + ".jpg");
        val o2 = from
                .flatMap(pic -> api.getDesc(pic.cid, pic.pid))
                .map(res -> {
                    if (res.isSuccessful())
                        return res.body();

                    throw new RuntimeException("no desc!");
                });

        val o3 = from
                .map(it -> it.cid);

        val o4 = from
                .map(it -> it.pid);

        val of = Observable.zip(o1, o2, o3, o4, CompositeImage::new)
                .toList()
                .subscribe(this::submit);
    }

    private void submit(List<CompositeImage> compositeImages) {
        getView().setRecycler(compositeImages);
    }
}
