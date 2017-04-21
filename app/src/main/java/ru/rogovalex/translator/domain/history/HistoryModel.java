package ru.rogovalex.translator.domain.history;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.TranslateParams;
import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface HistoryModel {
    Observable<List<Translation>> loadHistory();

    Observable<Boolean> updateHistory(Translation translation, String uiLangCode);

    Observable<List<Translation>> loadFromHistory(TranslateParams params);

    Observable<Boolean> clearHistory();
}
