package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import ru.rogovalex.translator.domain.translate.TranslateResult;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public interface FavoriteView {
    void onFavoriteLoading();

    void onFavoriteLoaded(List<TranslateResult> items);

    void onFavoriteLoadError(Throwable e);
}
