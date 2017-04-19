package ru.rogovalex.translator.domain;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.model.TranslateParams;

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
