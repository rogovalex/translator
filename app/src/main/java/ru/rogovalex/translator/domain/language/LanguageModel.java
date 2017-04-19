package ru.rogovalex.translator.domain.language;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface LanguageModel {
    Observable<List<Language>> loadLanguages(String uiLang);

    Observable<Boolean> updateLanguages(String uiLang, List<Language> languages);
}
