package ru.rogovalex.translator.domain.history;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface HistoryModel {
    Observable<List<Translation>> loadHistory();

    Observable<Boolean> updateHistory(Translation translation);

    Observable<List<Translation>> loadFromHistory(TranslateParams params);
}
