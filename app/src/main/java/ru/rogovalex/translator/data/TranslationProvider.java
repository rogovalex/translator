package ru.rogovalex.translator.data;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.model.TranslationParams;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:21
 */
public interface TranslationProvider {
    Observable<String> translate(TranslationParams params);

    Observable<List<Language>> languages(String uiLang);
}
