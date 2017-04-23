package ru.rogovalex.translator.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.TranslateProvider;
import ru.rogovalex.translator.domain.language.LanguageRepository;
import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:34
 */
public class TranslationLanguageRepository implements LanguageRepository {

    private final Database mDatabase;
    private final TranslateProvider mProvider;

    @Inject
    public TranslationLanguageRepository(Database database, TranslateProvider provider) {
        mDatabase = database;
        mProvider = provider;
    }

    @Override
    public Observable<List<Language>> loadLanguages(final String uiLang) {
        Observable<List<Language>> fromDatabase = Observable.just(uiLang)
                .map(mDatabase::getLanguages)
                .filter(list -> list.size() > 0);

        Observable<List<Language>> fromProvider = mProvider.languages(uiLang)
                .map(languages -> mDatabase.saveLanguages(uiLang, languages))
                .map(value -> mDatabase.getLanguages(uiLang));

        return Observable.concat(fromDatabase, fromProvider)
                .firstElement()
                .toObservable();
    }

    @Override
    public Observable<Boolean> updateLanguages(final String uiLang, final List<Language> languages) {
        return Observable.fromCallable(() -> mDatabase.saveLanguages(uiLang, languages));
    }
}
