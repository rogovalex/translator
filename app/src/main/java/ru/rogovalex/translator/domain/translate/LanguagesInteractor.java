package ru.rogovalex.translator.domain.translate;

import java.util.List;

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
public class LanguagesInteractor extends Interactor<List<Language>, String> {

    private final TranslateProvider mProvider;
    private final Storage mStorage;

    @Inject
    public LanguagesInteractor(@Named(DomainModule.JOB) Scheduler jobScheduler,
                               @Named(DomainModule.UI) Scheduler uiScheduler,
                               TranslateProvider translateProvider,
                               Storage storage) {
        super(jobScheduler, uiScheduler);
        mProvider = translateProvider;
        mStorage = storage;
    }

    @Override
    protected Observable<List<Language>> buildObservable(String uiLang) {
        // TODO set to storage / get from storage
        return mProvider.languages(uiLang);
    }
}
