package ru.rogovalex.translator.presentation.main.history;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.history.ClearHistoryInteractor;
import ru.rogovalex.translator.domain.history.LoadHistoryInteractor;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public class HistoryViewPresenter extends BasePresenter<HistoryView> {

    private final LoadHistoryInteractor mInteractor;
    private final ClearHistoryInteractor mClearInteractor;
    private final UpdateFavoriteInteractor mUpdateInteractor;

    private List<Translation> mItems;

    @Inject
    public HistoryViewPresenter(LoadHistoryInteractor interactor,
                                ClearHistoryInteractor clearInteractor,
                                UpdateFavoriteInteractor updateInteractor) {
        mInteractor = interactor;
        mClearInteractor = clearInteractor;
        mUpdateInteractor = updateInteractor;
        super.setView(sStubView);
    }

    @Override
    public HistoryView getStubView() {
        return sStubView;
    }

    public void loadHistory() {
        if (mItems != null) {
            getView().onHistoryLoaded(mItems);
            return;
        }

        getView().onHistoryLoading();

        if (mInteractor.isRunning()) {
            return;
        }

        mInteractor.execute(null, items -> {
            mItems = items;
            getView().onHistoryLoaded(items);
        }, e -> getView().onHistoryLoadError(e));
    }

    public void clearHistory() {
        mClearInteractor.execute(null, value -> {
            mItems = null;
            getView().onHistoryCleared();
        }, Functions.emptyConsumer());
    }

    public void updateFavorite(Translation item) {
        mUpdateInteractor.execute(item, Functions.emptyConsumer(),
                Functions.emptyConsumer());
    }

    public void cancel() {
        mInteractor.cancel();
        mClearInteractor.cancel();
        mUpdateInteractor.cancel();
    }

    private static HistoryView sStubView = new HistoryView() {
        @Override
        public void onHistoryLoading() {
        }

        @Override
        public void onHistoryLoaded(List<Translation> items) {
        }

        @Override
        public void onHistoryLoadError(Throwable e) {
        }

        @Override
        public void onHistoryCleared() {
        }
    };
}
