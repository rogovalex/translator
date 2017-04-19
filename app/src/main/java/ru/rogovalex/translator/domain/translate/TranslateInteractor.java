package ru.rogovalex.translator.domain.translate;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.domain.history.HistoryModel;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:18
 */
public class TranslateInteractor extends Interactor<Translation, TranslateParams> {

    private final TranslateProvider mTranslateProvider;
    private final DictionaryProvider mDictionaryProvider;
    private final HistoryModel mModel;

    @Inject
    public TranslateInteractor(@Named(DomainModule.JOB) Scheduler jobScheduler,
                               @Named(DomainModule.UI) Scheduler uiScheduler,
                               TranslateProvider translateProvider,
                               DictionaryProvider dictionaryProvider,
                               HistoryModel model) {
        super(jobScheduler, uiScheduler);
        mTranslateProvider = translateProvider;
        mDictionaryProvider = dictionaryProvider;
        mModel = model;
    }

    @Override
    protected Observable<Translation> buildObservable(final TranslateParams params) {
        return mModel.loadFromHistory(params)
                .flatMap(new Function<List<Translation>, Observable<Translation>>() {
                    @Override
                    public Observable<Translation> apply(List<Translation> translations) throws Exception {
                        if (!translations.isEmpty()) {
                            return Observable.just(translations.get(0));
                        }
                        return loadFromNetwork(params);
                    }
                });
    }

    private Observable<Translation> loadFromNetwork(final TranslateParams params) {
        return mTranslateProvider.translate(params)
                .zipWith(lookupDictionary(params), new BiFunction<String, List<Definition>, Translation>() {
                    @Override
                    public Translation apply(String translation, List<Definition> definitions) throws Exception {
                        return new Translation(params.getText(), params.getTextLang(),
                                translation, params.getTranslationLang(), definitions);
                    }
                })
                .flatMap(new Function<Translation, Observable<Translation>>() {
                    @Override
                    public Observable<Translation> apply(final Translation translation) throws Exception {
                        return mModel.updateHistory(translation)
                                .map(new Function<Boolean, Translation>() {
                                    @Override
                                    public Translation apply(Boolean aBoolean) throws Exception {
                                        return translation;
                                    }
                                })
                                .onErrorReturnItem(translation);
                    }
                });
    }

    private Observable<List<Definition>> lookupDictionary(TranslateParams params) {
        return mDictionaryProvider.lookup(params)
                .onErrorReturnItem(Collections.<Definition>emptyList());
    }
}
