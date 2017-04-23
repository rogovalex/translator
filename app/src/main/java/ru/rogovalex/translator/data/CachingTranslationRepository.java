package ru.rogovalex.translator.data;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.model.Definition;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.domain.model.TranslationParams;
import ru.rogovalex.translator.domain.translate.TranslationRepository;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 24.04.2017
 * Time: 0:00
 */
public class CachingTranslationRepository implements TranslationRepository {

    private final Database mDatabase;
    private final TranslationProvider mTranslateProvider;
    private final DictionaryProvider mDictionaryProvider;

    @Inject
    public CachingTranslationRepository(Database database, TranslationProvider translateProvider,
                                        DictionaryProvider dictionaryProvider) {
        mDatabase = database;
        mTranslateProvider = translateProvider;
        mDictionaryProvider = dictionaryProvider;
    }

    @Override
    public Observable<Translation> getTranslation(TranslationParams params) {
        Observable<Translation> fromDatabase = Observable.just(params)
                .map(mDatabase::getTranslations)
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0));

        Observable<Translation> fromProvider = mTranslateProvider.translate(params)
                .zipWith(lookupDictionary(params),
                        (translation, definitions) -> new Translation(params.getText(),
                                params.getTextLang(), translation,
                                params.getTranslationLang(), definitions));

        return Observable.concat(fromDatabase, fromProvider)
                .firstElement()
                .toObservable()
                .map(translation -> {
                    mDatabase.saveRecentTranslation(translation, params.getUiLangCode());
                    return translation;
                });
    }

    private Observable<List<Definition>> lookupDictionary(TranslationParams params) {
        return mDictionaryProvider.lookup(params)
                .onErrorReturnItem(Collections.emptyList());
    }
}
