package ru.rogovalex.translator.domain.history;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:10
 */
public interface HistoryRepository {
    Observable<List<Translation>> loadHistory();

    Observable<Boolean> clearHistory();
}
