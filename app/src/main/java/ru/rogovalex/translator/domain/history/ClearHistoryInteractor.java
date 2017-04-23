package ru.rogovalex.translator.domain.history;


import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:30
 */
public class ClearHistoryInteractor extends Interactor<Boolean, Void> {

    private final HistoryRepository mRepository;

    @Inject
    public ClearHistoryInteractor(@Named(DomainModule.LOCAL) Scheduler jobScheduler,
                                  @Named(DomainModule.UI) Scheduler uiScheduler,
                                  HistoryRepository repository) {
        super(jobScheduler, uiScheduler);
        mRepository = repository;
    }

    @Override
    protected Observable<Boolean> buildObservable(Void params) {
        return mRepository.clearHistory();
    }
}
