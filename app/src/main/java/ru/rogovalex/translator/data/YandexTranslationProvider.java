package ru.rogovalex.translator.data;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import ru.rogovalex.translator.data.api.TranslateApiService;
import ru.rogovalex.translator.data.api.YandexApiException;
import ru.rogovalex.translator.data.api.response.LanguagesResponse;
import ru.rogovalex.translator.data.api.response.TranslateResponse;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.model.TranslationParams;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:28
 */
public class YandexTranslationProvider implements TranslationProvider {

    private static final String API_KEY = "trnsl.1.1.20170401T123903Z.821a542db77c4103.831126fb02a869cdd86f9c876690e4c437f83f65";
    private static final String FORMAT_PLAIN = "plain";
    private static final String DISABLE_AUTO_DETERMINATION = "0";

    private final TranslateApiService mService;

    public YandexTranslationProvider(TranslateApiService service) {
        mService = service;
    }

    @Override
    public Observable<String> translate(final TranslationParams params) {
        return mService.translate(API_KEY, params.getText(),
                params.getTextLang() + "-" + params.getTranslationLang(),
                FORMAT_PLAIN, DISABLE_AUTO_DETERMINATION)
                .flatMap(this::handleTranslateResponse);
    }

    @Override
    public Observable<List<Language>> languages(String uiLang) {
        return mService.languages(API_KEY, uiLang)
                .flatMap(this::handleLanguagesResponse)
                .flatMapIterable(Map::entrySet)
                .map(entry -> new Language(entry.getKey(), entry.getValue()))
                .toList()
                .toObservable();
    }

    private Observable<String> handleTranslateResponse(TranslateResponse response) {
        if (response.getCode() != 200) {
            return Observable.error(new YandexApiException(response.getCode()));
        }
        if (response.getText() == null || response.getText().length == 0) {
            return Observable.error(new YandexApiException(422));
        }
        return Observable.just(response.getText()[0]);
    }

    private Observable<Map<String, String>> handleLanguagesResponse(LanguagesResponse response) {
        if (response.getCode() != 200) {
            return Observable.error(new YandexApiException(response.getCode()));
        }
        return Observable.just(response.getLangs());
    }
}
