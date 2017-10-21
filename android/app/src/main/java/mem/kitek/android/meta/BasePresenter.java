package mem.kitek.android.meta;

import javax.inject.Inject;

import lombok.Getter;

/**
 * Created by cat on 10/20/17.
 */

public abstract class BasePresenter<T> {
    private final @Getter
    T view;

    protected BasePresenter(T view) {
        this.view = view;
    }
}
