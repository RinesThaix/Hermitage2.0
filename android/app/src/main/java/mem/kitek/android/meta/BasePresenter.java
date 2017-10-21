package mem.kitek.android.meta;

import javax.inject.Inject;

import lombok.Getter;
import lombok.val;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by cat on 10/20/17.
 */

public abstract class BasePresenter<T> {
    private final @Getter
    T view;
    private final CompositeSubscription subscription = new CompositeSubscription();
    protected BasePresenter(T view) {
        this.view = view;
    }

    protected void register(Subscription... subs) {
        for (val i : subs) {
            subscription.add(i);
        }
    }

    public void dropAll() {
        subscription.clear();
    }
}
