package ru.rogovalex.translator.domain.favorite;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface FavoriteModel {
    Observable<List<Translation>> loadFavorite();

    Observable<Boolean> updateFavorite(Translation translation);
}
