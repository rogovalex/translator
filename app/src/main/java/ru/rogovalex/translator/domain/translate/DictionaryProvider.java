package ru.rogovalex.translator.domain.translate;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.api.Entry;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:21
 */
public interface DictionaryProvider {
    Observable<List<Entry>> lookup(TranslateParams params);
}