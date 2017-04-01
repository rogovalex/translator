package ru.rogovalex.translator.domain.translate;

import io.reactivex.Observable;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:21
 */
public interface TranslateProvider {
    Observable<TranslateResult> translate(TranslateParams params);
}
