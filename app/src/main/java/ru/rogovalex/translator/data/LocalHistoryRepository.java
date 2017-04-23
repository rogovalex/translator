package ru.rogovalex.translator.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.history.HistoryRepository;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.domain.model.TranslationParams;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:08
 */
public class LocalHistoryRepository implements HistoryRepository {

    private final Database mDatabase;

    @Inject
    public LocalHistoryRepository(Database database) {
        mDatabase = database;
    }

    @Override
    public Observable<List<Translation>> loadHistory() {
        return Observable.fromCallable(mDatabase::getRecentTranslations);
    }

    @Override
    public Observable<Boolean> updateHistory(Translation translation, String uiLangCode) {
        return Observable.fromCallable(() -> mDatabase.saveRecentTranslation(translation, uiLangCode));
    }

    @Override
    public Observable<List<Translation>> loadFromHistory(TranslationParams params) {
        return Observable.just(params)
                .map(mDatabase::getTranslations);
    }

    @Override
    public Observable<Boolean> clearHistory() {
        return Observable.fromCallable(mDatabase::removeRecentTranslations);
    }
}
