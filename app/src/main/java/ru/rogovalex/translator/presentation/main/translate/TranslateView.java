package ru.rogovalex.translator.presentation.main.translate;

import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:33
 */
public interface TranslateView {
    void onTranslating();

    void onTranslated(Translation translation);

    void onTranslateError(Throwable e);

    void onTranslationDirectionChanged(String text);
}
