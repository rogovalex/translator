package ru.rogovalex.translator.domain.translate;

import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 23.04.2017
 * Time: 14:19
 */
public interface TranslationPreferences {
    Language getSourceLanguage();

    Language getTranslationLanguage();

    void setLanguages(Language source, Language translation);

    void setSourceLanguage(Language language);

    void setTranslationLanguage(Language language);
}
