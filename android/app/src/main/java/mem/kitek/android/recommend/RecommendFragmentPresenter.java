package mem.kitek.android.recommend;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import lombok.val;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.main.MainActivity;
import mem.kitek.android.map.MapFragment_;
import mem.kitek.android.meta.BasePresenter;
import mem.kitek.android.meta.scope.FragmentScope;
import mem.kitek.android.service.KiteqAPI;
import mem.kitek.android.service.RawServiceAPI;
import mem.kitek.android.service.ServiceAPI;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cat on 10/22/17.
 */
@FragmentScope
class RecommendFragmentPresenter extends BasePresenter<RecommendFragment> {
    private static final int AMOUNT = 15;
    private final ServiceAPI api;
    private final RawServiceAPI rawApi;
    private List<ApiData.Picture> curPictureList;

    @Inject
    public RecommendFragmentPresenter(RecommendFragment view, ServiceAPI api, RawServiceAPI rawApi) {
        super(view);
        this.api = api;
        this.rawApi = rawApi;
    }

    public void requestImages() {
        val s = api.getPictureInfo(AMOUNT)
                .map(KiteqAPI::unwrap)
                .map(ApiData.PictureData::getPictures)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::processData, e -> {}, () -> {});
    }

    private void processData(List<ApiData.Picture> pictures) {
        Observable<ApiData.Picture> from = Observable.from(pictures);
        val o1 = from
                .map(pic -> "http://kitek.kostya.sexy/"
                                + "categories/"
                                + pic.cid + "/"
                                + pic.pid + ".jpg");
        val o2 = from
                .observeOn(Schedulers.io())
                .flatMap(pic -> rawApi.getDesc(pic.cid, pic.pid))
                .map(res -> {
                    if (res.isSuccessful())
                        return res.body();

                    Log.d("TAG", "processData: NOT SUCC BECAUSE: " + res.code());
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread());

        val o3 = from
                .map(it -> it.cid);

        val o4 = from
                .map(it -> it.pid);

        Subscription of = Observable.zip(o1, o2, o3, o4, CompositeImage::new)
                .toList()
                .subscribe(this::submit);

        register(of);
    }

    private void submit(List<CompositeImage> compositeImages) {
        getView().setRecycler(compositeImages);
    }

    public void resolveLikes(List<CompositeImage> imageList) {
        val a = (MainActivity) getView().getActivity(); // fuck yeh.
        val f = MapFragment_.builder().resolution(new CompositeImage.Holder(imageList)).build();

        a.toFragment(f, 0);
    }
}
