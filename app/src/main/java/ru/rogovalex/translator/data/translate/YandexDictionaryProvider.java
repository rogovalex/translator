package ru.rogovalex.translator.data.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.api.DictionaryApiService;
import ru.rogovalex.translator.api.DictionaryResponse;
import ru.rogovalex.translator.api.Entry;
import ru.rogovalex.translator.domain.translate.DictionaryProvider;
import ru.rogovalex.translator.domain.translate.TranslateParams;

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
    public Observable<List<Entry>> lookup(final TranslateParams params) {
        return mService.lookup(API_KEY, params.getText(),
                params.getTextLang() + "-" + params.getTranslationLang(),
                UI_LANG, SHORT_POS)
                .flatMap(new Function<DictionaryResponse, Observable<List<Entry>>>() {
                    @Override
                    public Observable<List<Entry>> apply(DictionaryResponse response) throws Exception {
                        if (response.getCode() != 200) {
                            return Observable.error(new ApiException(response.getCode()));
                        }
                        List<Entry> result = response.getEntries() != null
                                ? Arrays.asList(response.getEntries())
                                : new ArrayList<Entry>();
                        return Observable.just(result);
                    }
                });
    }
}
