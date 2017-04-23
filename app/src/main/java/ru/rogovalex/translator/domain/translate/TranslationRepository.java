package ru.rogovalex.translator.domain.translate;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.domain.model.TranslationParams;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 23.04.2017
 * Time: 23:44
 */
public interface TranslationRepository {
    Observable<Translation> getTranslation(TranslationParams params);
}
