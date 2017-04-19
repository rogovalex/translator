package ru.rogovalex.translator.domain;

import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.domain.model.Definition;
import ru.rogovalex.translator.domain.model.TranslateParams;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:21
 */
public interface DictionaryProvider {
    Observable<List<Definition>> lookup(TranslateParams params);
}