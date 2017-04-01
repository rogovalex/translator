package ru.rogovalex.translator.presentation.translate;

import ru.rogovalex.translator.domain.translate.TranslateResult;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:33
 */
public interface TranslateView {
    void onTranslating();

    void onTranslated(TranslateResult translation);

    void onTranslateError(Throwable e);
}