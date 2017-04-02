package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.rogovalex.translator.domain.translate.HistoryInteractor;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public class HistoryViewPresenter extends BasePresenter<HistoryView> {

    private final HistoryInteractor mInteractor;

    @Inject
    public HistoryViewPresenter(HistoryInteractor interactor) {
        mInteractor = interactor;
    }

    public void loadHistory() {
        mInteractor.execute(null, new Consumer<List<TranslateResult>>() {
            @Override
            public void accept(List<TranslateResult> items) throws Exception {
                getView().onHistoryLoaded(items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                getView().onHistoryLoadError(e);
            }
        });
    }
}
