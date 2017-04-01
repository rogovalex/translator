package ru.rogovalex.translator.presentation.translate;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:33
 */
public interface TranslateView {
    void onTranslating();

    void onTranslated(String translation);

    void onTranslateError(Throwable e);
}
