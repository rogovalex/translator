package ru.rogovalex.translator.domain.history;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.domain.translate.Translation;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:30
 */
public class LoadHistoryInteractor extends Interactor<List<Translation>, Void> {

    private final HistoryModel mModel;

    @Inject
    public LoadHistoryInteractor(@Named(DomainModule.LOCAL) Scheduler jobScheduler,
                                 @Named(DomainModule.UI) Scheduler uiScheduler,
                                 HistoryModel model) {
        super(jobScheduler, uiScheduler);
        mModel = model;
    }

    @Override
    protected Observable<List<Translation>> buildObservable(Void params) {
        return mModel.loadHistory();
    }
}
