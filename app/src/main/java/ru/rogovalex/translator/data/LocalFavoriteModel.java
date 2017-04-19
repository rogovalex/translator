package ru.rogovalex.translator.data;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.favorite.FavoriteModel;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:08
 */
public class LocalFavoriteModel implements FavoriteModel {

    private final Database mDatabase;

    @Inject
    public LocalFavoriteModel(Database database) {
        mDatabase = database;
    }

    @Override
    public Observable<List<Translation>> loadFavorite() {
        return Observable.fromCallable(new Callable<List<Translation>>() {
            @Override
            public List<Translation> call() throws Exception {
                return mDatabase.getFavoriteTranslations();
            }
        });
    }

    @Override
    public Observable<Boolean> updateFavorite(final Translation translation) {
        return Observable.just(translation)
                .map(new Function<Translation, Boolean>() {
                    @Override
                    public Boolean apply(Translation translation) throws Exception {
                        return mDatabase.updateFavoriteTranslation(translation);
                    }
                });
    }
}
