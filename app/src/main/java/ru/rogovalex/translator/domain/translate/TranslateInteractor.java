package ru.rogovalex.translator.domain.translate;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.DictionaryProvider;
import ru.rogovalex.translator.domain.TranslateProvider;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.domain.history.HistoryModel;
import ru.rogovalex.translator.domain.model.Definition;
import ru.rogovalex.translator.domain.model.TranslateParams;
import ru.rogovalex.translator.domain.model.Translation;
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
        Observable<Translation> fromModel = mModel.loadFromHistory(params)
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0));

        Observable<Translation> fromApi = mTranslateProvider.translate(params)
                .zipWith(lookupDictionary(params),
                        (translation, definitions) -> new Translation(params.getText(),
                                params.getTextLang(), translation,
                                params.getTranslationLang(), definitions));

        return Observable.concat(fromModel, fromApi)
                .firstElement()
                .toObservable()
                .flatMap(translation -> updateHistory(translation, params.getUiLangCode()));
    }

    private Observable<List<Definition>> lookupDictionary(TranslateParams params) {
        return mDictionaryProvider.lookup(params)
                .onErrorReturnItem(Collections.emptyList());
    }

    private Observable<Translation> updateHistory(Translation translation, String uiLangCode) {
        return mModel.updateHistory(translation, uiLangCode)
                .map(value -> translation)
                .onErrorReturnItem(translation);
    }
}
