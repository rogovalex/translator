package ru.rogovalex.translator.domain.language;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.domain.translate.Language;
import ru.rogovalex.translator.domain.translate.TranslateProvider;
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
                .flatMap(new Function<List<Language>, Observable<List<Language>>>() {
                    @Override
                    public Observable<List<Language>> apply(List<Language> languages) throws Exception {
                        if (!languages.isEmpty()) {
                            return Observable.just(languages);
                        }
                        return loadFromNetwork(uiLang);
                    }
                });
    }

    private Observable<List<Language>> loadFromNetwork(final String uiLang) {
        return mProvider.languages(uiLang)
                .flatMap(new Function<List<Language>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(List<Language> languages) throws Exception {
                        return mModel.updateLanguages(uiLang, languages);
                    }
                })
                .flatMap(new Function<Boolean, Observable<List<Language>>>() {
                    @Override
                    public Observable<List<Language>> apply(Boolean value) throws Exception {
                        return mModel.loadLanguages(uiLang);
                    }
                });
    }
}
