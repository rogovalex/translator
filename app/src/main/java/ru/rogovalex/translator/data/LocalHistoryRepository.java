package ru.rogovalex.translator.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.history.HistoryRepository;
import ru.rogovalex.translator.domain.model.Translation;

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
    public Observable<Boolean> clearHistory() {
        return Observable.fromCallable(mDatabase::removeRecentTranslations);
    }
}
