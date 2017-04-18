package ru.rogovalex.translator.domain.translate;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    protected Observable<List<Language>> buildObservable(final String uiLang) {
        return Observable.fromCallable(new Callable<List<Language>>() {
            @Override
            public List<Language> call() throws Exception {
                return mStorage.getLanguages(uiLang);
            }
        })
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
                .doOnNext(new Consumer<List<Language>>() {
                    @Override
                    public void accept(List<Language> languages) throws Exception {
                        mStorage.saveLanguages(uiLang, languages);
                    }
                })
                .map(new Function<List<Language>, List<Language>>() {
                    @Override
                    public List<Language> apply(List<Language> languages) throws Exception {
                        return mStorage.getLanguages(uiLang);
                    }
                });
    }
}
