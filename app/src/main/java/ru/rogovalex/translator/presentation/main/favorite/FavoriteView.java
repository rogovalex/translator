package ru.rogovalex.translator.presentation.main.favorite;

import java.util.List;

import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public interface FavoriteView {
    void onFavoriteLoading();

    void onFavoriteLoaded(List<Translation> items);

    void onFavoriteLoadError(Throwable e);
}
