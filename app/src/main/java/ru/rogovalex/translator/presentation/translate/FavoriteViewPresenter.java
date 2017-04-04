package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.translate.LoadFavoritesInteractor;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.domain.translate.UpdateFavoriteInteractor;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public class FavoriteViewPresenter extends BasePresenter<FavoriteView> {

    private final LoadFavoritesInteractor mInteractor;
    private final UpdateFavoriteInteractor mUpdateInteractor;

    private boolean mLoading;

    @Inject
    public FavoriteViewPresenter(LoadFavoritesInteractor interactor,
                                 UpdateFavoriteInteractor updateInteractor) {
        mInteractor = interactor;
        mUpdateInteractor = updateInteractor;
        super.setView(sStubView);
    }

    public void loadFavorite() {
        getView().onFavoriteLoading();

        if (mLoading) {
            return;
        }

        mLoading = true;
        mInteractor.execute(null, new Consumer<List<TranslateResult>>() {
            @Override
            public void accept(List<TranslateResult> items) throws Exception {
                mLoading = false;
                getView().onFavoriteLoaded(items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                mLoading = false;
                getView().onFavoriteLoadError(e);
            }
        });
    }

    public void updateFavorite(TranslateResult item) {
        mUpdateInteractor.execute(item, Functions.<Boolean>emptyConsumer());
    }

    public void cancel() {
        mLoading = false;
        mInteractor.cancel();
        mUpdateInteractor.cancel();
    }

    private static FavoriteView sStubView = new FavoriteView() {
        @Override
        public void onFavoriteLoading() {
        }

        @Override
        public void onFavoriteLoaded(List<TranslateResult> items) {
        }

        @Override
        public void onFavoriteLoadError(Throwable e) {
        }
    };
}
