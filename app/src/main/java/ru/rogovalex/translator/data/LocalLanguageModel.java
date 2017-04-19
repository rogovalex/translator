package ru.rogovalex.translator.data;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.language.LanguageModel;
import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:34
 */
public class LocalLanguageModel implements LanguageModel {

    private final Database mDatabase;

    @Inject
    public LocalLanguageModel(Database database) {
        mDatabase = database;
    }

    @Override
    public Observable<List<Language>> loadLanguages(final String uiLang) {
        return Observable.fromCallable(new Callable<List<Language>>() {
            @Override
            public List<Language> call() throws Exception {
                return mDatabase.getLanguages(uiLang);
            }
        });
    }

    @Override
    public Observable<Boolean> updateLanguages(final String uiLang, final List<Language> languages) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return mDatabase.saveLanguages(uiLang, languages);
            }
        });
    }
}
