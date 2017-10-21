package mem.kitek.android.recommend;

import javax.inject.Inject;

import mem.kitek.android.meta.BasePresenter;
import mem.kitek.android.meta.scope.ActivityScope;

/**
 * Created by cat on 10/20/17.
 */

@ActivityScope
class RecommendActivityPresenter extends BasePresenter<RecommendActivity> {
    @Inject
    protected RecommendActivityPresenter(RecommendActivity view) {
        super(view);
    }

    public void requestCards() {
        getView().setCards(new Object[10]);
    }
}
