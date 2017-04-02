package ru.rogovalex.translator.data.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.api.DictionaryApiService;
import ru.rogovalex.translator.api.DictionaryEntry;
import ru.rogovalex.translator.api.DictionaryResponse;
import ru.rogovalex.translator.api.DictionaryTranslation;
import ru.rogovalex.translator.domain.translate.Definition;
import ru.rogovalex.translator.domain.translate.DictionaryProvider;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:28
 */
public class YandexDictionaryProvider implements DictionaryProvider {

    private static final String API_KEY = "dict.1.1.20170401T161610Z.c2b221b76a508a30.06c55e1216f351abaedfbd9c37f1964e5ff7b21a";
    private static final String UI_LANG = "ru";
    private static final int SHORT_POS = 2;

    private final DictionaryApiService mService;

    @Inject
    public YandexDictionaryProvider(DictionaryApiService service) {
        mService = service;
    }

    @Override
    public Observable<List<Definition>> lookup(final TranslateParams params) {
        return mService.lookup(API_KEY, params.getText(),
                params.getTextLang() + "-" + params.getTranslationLang(),
                UI_LANG, SHORT_POS)
                .flatMap(new Function<DictionaryResponse, Observable<DictionaryResponse>>() {
                    @Override
                    public Observable<DictionaryResponse> apply(DictionaryResponse response) throws Exception {
                        if (response.getCode() != 200) {
                            return Observable.error(new ApiException(response.getCode()));
                        }
                        return Observable.just(response);
                    }
                })
                .flatMapIterable(new Function<DictionaryResponse, Iterable<DictionaryEntry>>() {
                    @Override
                    public Iterable<DictionaryEntry> apply(DictionaryResponse response) throws Exception {
                        return response.getEntries() != null
                                ? Arrays.asList(response.getEntries())
                                : new ArrayList<DictionaryEntry>();
                    }
                })
                .map(new Function<DictionaryEntry, Definition>() {
                    @Override
                    public Definition apply(DictionaryEntry entry) throws Exception {
                        Definition result = new Definition(entry.getText(),
                                entry.getTranscription(), entry.getPos(),
                                new ArrayList<Translation>());

                        if (entry.getTranslations() != null) {
                            for (DictionaryTranslation t : entry.getTranslations()) {
                                result.getTranslations().add(convert(t));
                            }
                        }

                        return result;
                    }
                })
                .toList()
                .toObservable();
    }

    private Translation convert(DictionaryTranslation t) {
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

        return new Translation(synonyms, meanings, examples);
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
