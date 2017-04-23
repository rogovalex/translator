package ru.rogovalex.translator.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.language.LanguagesRepository;
import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:34
 */
public class CachingLanguagesRepository implements LanguagesRepository {

    private final Database mDatabase;
    private final LanguagesProvider mProvider;

    @Inject
    public CachingLanguagesRepository(Database database, LanguagesProvider provider) {
        mDatabase = database;
        mProvider = provider;
    }

    @Override
    public Observable<List<Language>> getLanguages(final String uiLang) {
        Observable<List<Language>> fromDatabase = Observable.just(uiLang)
                .map(mDatabase::getLanguages)
                .filter(list -> list.size() > 0);

        Observable<List<Language>> fromProvider = mProvider.languages(uiLang)
                .map(languages -> mDatabase.saveLanguages(uiLang, languages))
                // запрос из БД возвращает отсортированные данные
                .map(value -> mDatabase.getLanguages(uiLang));

        return Observable.concat(fromDatabase, fromProvider)
                .firstElement()
                .toObservable();
    }
}
