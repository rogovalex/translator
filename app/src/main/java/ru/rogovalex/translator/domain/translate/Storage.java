package ru.rogovalex.translator.domain.translate;

import java.util.List;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface Storage {
    List<Translation> getRecentTranslations();

    boolean checkFavorite(Translation translation);

    void saveRecentTranslation(Translation translation);

    List<Language> getLanguages(String uiLang);

    void saveLanguages(String uiLang, List<Language> languages);
}
