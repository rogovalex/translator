package ru.rogovalex.translator.domain.language;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.TranslateProvider;
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

    private final TranslateProvider mProvider;
    private final LanguageModel mModel;

    @Inject
    public LoadLanguagesInteractor(@Named(DomainModule.JOB) Scheduler jobScheduler,
                                   @Named(DomainModule.UI) Scheduler uiScheduler,
                                   TranslateProvider provider,
                                   LanguageModel model) {
        super(jobScheduler, uiScheduler);
        mProvider = provider;
        mModel = model;
    }

    @Override
    protected Observable<List<Language>> buildObservable(final String uiLang) {
        return mModel.loadLanguages(uiLang)
                .flatMap(languages -> loadFromNetworkIfEmpty(uiLang, languages));
    }

    private Observable<List<Language>> loadFromNetworkIfEmpty(String uiLang, List<Language> languages) {
        if (languages.isEmpty()) {
            return loadFromNetwork(uiLang);
        }
        return Observable.just(languages);
    }

    private Observable<List<Language>> loadFromNetwork(final String uiLang) {
        return mProvider.languages(uiLang)
                .flatMap(languages -> mModel.updateLanguages(uiLang, languages))
                .flatMap(value -> mModel.loadLanguages(uiLang));
    }
}
