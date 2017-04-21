package ru.rogovalex.translator.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.api.DictionaryApiService;
import ru.rogovalex.translator.api.response.DictionaryEntry;
import ru.rogovalex.translator.api.response.DictionaryResponse;
import ru.rogovalex.translator.api.response.DictionaryTranslation;
import ru.rogovalex.translator.domain.DictionaryProvider;
import ru.rogovalex.translator.domain.model.Definition;
import ru.rogovalex.translator.domain.model.DefinitionOption;
import ru.rogovalex.translator.domain.model.TranslateParams;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:28
 */
public class YandexDictionaryProvider implements DictionaryProvider {

    private static final String API_KEY = "dict.1.1.20170401T161610Z.c2b221b76a508a30.06c55e1216f351abaedfbd9c37f1964e5ff7b21a";
    private static final int SHORT_POS = 2;

    private final DictionaryApiService mService;

    public YandexDictionaryProvider(DictionaryApiService service) {
        mService = service;
    }

    @Override
    public Observable<List<Definition>> lookup(final TranslateParams params) {
        return mService.lookup(API_KEY, params.getText(),
                params.getTextLang() + "-" + params.getTranslationLang(),
                params.getUiLangCode(), SHORT_POS)
                .flatMap(this::handleDictionaryResponse)
                .flatMapIterable(list -> list)
                .map(this::convert)
                .toList()
                .toObservable();
    }

    private Observable<List<DictionaryEntry>> handleDictionaryResponse(DictionaryResponse response) {
        if (response.getCode() != 200) {
            return Observable.error(new ApiException(response.getCode()));
        }
        return Observable.just(response.getEntries() != null
                ? Arrays.asList(response.getEntries())
                : new ArrayList<>());
    }

    private Definition convert(DictionaryEntry entry) {
        Definition result = new Definition(entry.getText(),
                notNull(entry.getTranscription()),
                notNull(entry.getPos()),
                new ArrayList<>());

        if (entry.getTranslations() != null) {
            for (DictionaryTranslation t : entry.getTranslations()) {
                result.getDefinitionOptions().add(convert(t));
            }
        }

        return result;
    }

    private String notNull(String value) {
        return value == null ? "" : value;
    }

    private DefinitionOption convert(DictionaryTranslation t) {
        String synonyms;
        if (t.getSynonyms() != null
                && t.getSynonyms().length > 0) {
            synonyms = join(t.getText(),
                    t.getSynonyms());
        } else {
            synonyms = t.getText();
        }

        String meanings = "";
        if (t.getMeanings() != null
                && t.getMeanings().length > 0) {
            meanings = join("", t.getMeanings());
        }

        String examples = "";
        if (t.getExamples() != null
                && t.getExamples().length > 0) {
            examples = join("", t.getExamples());
        }

        return new DefinitionOption(synonyms, meanings, examples);
    }

    private String join(String value, DictionaryTranslation... items) {
        StringBuilder sb = new StringBuilder(value);
        for (DictionaryTranslation item : items) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(item.getText());
        }
        return sb.toString();
    }
}
