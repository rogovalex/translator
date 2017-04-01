package ru.rogovalex.translator.domain.translate;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:18
 */
public class TranslateInteractor extends Interactor<TranslateResult, TranslateParams> {

    private final TranslateProvider mProvider;

    @Inject
    public TranslateInteractor(@Named(DomainModule.JOB) Scheduler jobScheduler,
                               @Named(DomainModule.UI) Scheduler uiScheduler,
                               TranslateProvider provider) {
        super(jobScheduler, uiScheduler);
        mProvider = provider;
    }

    @Override
    protected Observable<TranslateResult> buildObservable(TranslateParams params) {
        return mProvider.translate(params);
    }
}
