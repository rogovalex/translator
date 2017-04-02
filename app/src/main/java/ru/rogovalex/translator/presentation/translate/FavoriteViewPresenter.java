package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.rogovalex.translator.domain.translate.LoadFavoritesInteractor;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public class FavoriteViewPresenter extends BasePresenter<FavoriteView> {

    private final LoadFavoritesInteractor mInteractor;

    @Inject
    public FavoriteViewPresenter(LoadFavoritesInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadFavorite() {
        mInteractor.execute(null, new Consumer<List<TranslateResult>>() {
            @Override
            public void accept(List<TranslateResult> items) throws Exception {
                getView().onFavoriteLoaded(items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                getView().onFavoriteLoadError(e);
            }
        });
    }
}
