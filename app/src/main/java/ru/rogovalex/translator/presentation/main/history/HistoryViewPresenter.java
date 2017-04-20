package ru.rogovalex.translator.presentation.main.history;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
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
    private final UpdateFavoriteInteractor mUpdateInteractor;

    private List<Translation> mItems;

    private boolean mLoading;

    @Inject
    public HistoryViewPresenter(LoadHistoryInteractor interactor,
                                UpdateFavoriteInteractor updateInteractor) {
        mInteractor = interactor;
        mUpdateInteractor = updateInteractor;
        super.setView(sStubView);
    }

    public void loadHistory() {
        if (mItems != null) {
            getView().onHistoryLoaded(mItems);
            return;
        }

        getView().onHistoryLoading();

        if (mLoading) {
            return;
        }

        mLoading = true;
        mInteractor.execute(null, items -> {
            mLoading = false;
            mItems = items;
            getView().onHistoryLoaded(items);
        }, e -> {
            mLoading = false;
            getView().onHistoryLoadError(e);
        });
    }

    public void updateFavorite(Translation item) {
        mUpdateInteractor.execute(item, Functions.emptyConsumer(),
                Functions.emptyConsumer());
    }

    public void cancel() {
        mLoading = false;
        mInteractor.cancel();
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
    };
}
