package ru.rogovalex.translator.domain.translate;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:18
 */
public class TranslateInteractor extends Interactor<TranslateResult, TranslateParams> {

    private final TranslateProvider mTranslateProvider;
    private final DictionaryProvider mDictionaryProvider;

    @Inject
    public TranslateInteractor(@Named(DomainModule.JOB) Scheduler jobScheduler,
                               @Named(DomainModule.UI) Scheduler uiScheduler,
                               TranslateProvider translateProvider,
                               DictionaryProvider dictionaryProvider) {
        super(jobScheduler, uiScheduler);
        mTranslateProvider = translateProvider;
        mDictionaryProvider = dictionaryProvider;
    }

    @Override
    protected Observable<TranslateResult> buildObservable(final TranslateParams params) {
        return mTranslateProvider.translate(params)
                .zipWith(lookupDictionary(params), new BiFunction<String, List<Definition>, TranslateResult>() {
                    @Override
                    public TranslateResult apply(String translation, List<Definition> definitions) throws Exception {
                        return new TranslateResult(params.getText(), params.getTextLang(),
                                translation, params.getTranslationLang(), definitions);
                    }
                });
    }

    private Observable<List<Definition>> lookupDictionary(TranslateParams params) {
        return mDictionaryProvider.lookup(params)
                .onErrorReturnItem(Collections.<Definition>emptyList());
    }
}
