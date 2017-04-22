package ru.rogovalex.translator.presentation.main.favorite;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.favorite.LoadFavoriteInteractor;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public class FavoriteViewPresenter extends BasePresenter<FavoriteView> {

    private final LoadFavoriteInteractor mInteractor;
    private final UpdateFavoriteInteractor mUpdateInteractor;

    private List<Translation> mItems;

    @Inject
    public FavoriteViewPresenter(LoadFavoriteInteractor interactor,
                                 UpdateFavoriteInteractor updateInteractor) {
        mInteractor = interactor;
        mUpdateInteractor = updateInteractor;
        super.setView(sStubView);
    }

    public void loadFavorite() {
        if (mItems != null) {
            getView().onFavoriteLoaded(mItems);
            return;
        }

        getView().onFavoriteLoading();

        if (mInteractor.isRunning()) {
            return;
        }

        mInteractor.execute(null, items -> {
            mItems = items;
            getView().onFavoriteLoaded(items);
        }, e -> getView().onFavoriteLoadError(e));
    }

    public void updateFavorite(Translation item) {
        mUpdateInteractor.execute(item, Functions.emptyConsumer(),
                Functions.emptyConsumer());
    }

    public void cancel() {
        mInteractor.cancel();
        mUpdateInteractor.cancel();
    }

    private static FavoriteView sStubView = new FavoriteView() {
        @Override
        public void onFavoriteLoading() {
        }

        @Override
        public void onFavoriteLoaded(List<Translation> items) {
        }

        @Override
        public void onFavoriteLoadError(Throwable e) {
        }
    };
}
