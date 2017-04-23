package ru.rogovalex.translator.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 19.04.2017
 * Time: 13:08
 */
public class LocalFavoriteRepository implements FavoriteRepository {

    private final Database mDatabase;

    @Inject
    public LocalFavoriteRepository(Database database) {
        mDatabase = database;
    }

    @Override
    public Observable<List<Translation>> loadFavorite() {
        return Observable.fromCallable(mDatabase::getFavoriteTranslations);
    }

    @Override
    public Observable<Boolean> updateFavorite(final Translation translation) {
        return Observable.just(translation)
                .map(mDatabase::updateFavoriteTranslation);
    }
}
