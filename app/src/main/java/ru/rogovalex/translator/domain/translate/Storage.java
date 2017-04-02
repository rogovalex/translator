package ru.rogovalex.translator.domain.translate;

import java.util.List;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface Storage {
    List<TranslateResult> getRecentTranslations();

    List<TranslateResult> getFavoriteTranslations();

    void saveRecentTranslation(TranslateResult translation);

    void saveFavoriteTranslation(TranslateResult translation);
}
