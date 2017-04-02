package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.translate.LoadHistoryInteractor;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.domain.translate.UpdateFavoriteInteractor;
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

    private boolean mLoading;

    @Inject
    public HistoryViewPresenter(LoadHistoryInteractor interactor,
                                UpdateFavoriteInteractor updateInteractor) {
        mInteractor = interactor;
        mUpdateInteractor = updateInteractor;
    }

    public void loadHistory() {
        getView().onHistoryLoading();

        if (mLoading) {
            return;
        }

        mLoading = true;
        mInteractor.execute(null, new Consumer<List<TranslateResult>>() {
            @Override
            public void accept(List<TranslateResult> items) throws Exception {
                mLoading = false;
                getView().onHistoryLoaded(items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                mLoading = false;
                getView().onHistoryLoadError(e);
            }
        });
    }

    public void updateFavorite(TranslateResult item) {
        mUpdateInteractor.execute(item, Functions.<Boolean>emptyConsumer());
    }
}
