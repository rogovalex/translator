package ru.rogovalex.translator.domain.translate;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:21
 */
public interface TranslateProvider {
    Observable<String> translate(TranslateParams params);

    Observable<List<Language>> languages(String uiLang);
}
