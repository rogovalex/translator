package ru.rogovalex.translator.domain.language;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:18
 */
public class LoadLanguagesInteractor extends Interactor<List<Language>, String> {

    private final LanguagesRepository mRepository;

    @Inject
    public LoadLanguagesInteractor(@Named(DomainModule.JOB) Scheduler jobScheduler,
                                   @Named(DomainModule.UI) Scheduler uiScheduler,
                                   LanguagesRepository repository) {
        super(jobScheduler, uiScheduler);
        mRepository = repository;
    }

    @Override
    protected Observable<List<Language>> buildObservable(final String uiLang) {
        return mRepository.getLanguages(uiLang);
    }
}
