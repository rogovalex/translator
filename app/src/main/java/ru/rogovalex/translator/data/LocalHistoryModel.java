package ru.rogovalex.translator.data;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.history.HistoryModel;
import ru.rogovalex.translator.domain.model.TranslateParams;
import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:08
 */
public class LocalHistoryModel implements HistoryModel {

    private final Database mDatabase;

    @Inject
    public LocalHistoryModel(Database database) {
        mDatabase = database;
    }

    @Override
    public Observable<List<Translation>> loadHistory() {
        return Observable.fromCallable(new Callable<List<Translation>>() {
            @Override
            public List<Translation> call() throws Exception {
                return mDatabase.getRecentTranslations();
            }
        });
    }

    @Override
    public Observable<Boolean> updateHistory(Translation translation) {
        return Observable.just(translation)
                .map(new Function<Translation, Boolean>() {
                    @Override
                    public Boolean apply(Translation translation) throws Exception {
                        return mDatabase.saveRecentTranslation(translation);
                    }
                });
    }

    @Override
    public Observable<List<Translation>> loadFromHistory(TranslateParams params) {
        return Observable.just(params)
                .map(new Function<TranslateParams, List<Translation>>() {
                    @Override
                    public List<Translation> apply(TranslateParams params) throws Exception {
                        return mDatabase.getTranslations(params);
                    }
                });
    }
}
