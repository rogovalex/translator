package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public interface LanguagesView {
    void onLanguagesLoading();

    void onLanguagesLoaded(List<Language> items);

    void onLanguagesLoadError(Throwable e);
}
