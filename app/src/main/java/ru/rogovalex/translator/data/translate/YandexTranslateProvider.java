package ru.rogovalex.translator.data.translate;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.api.TranslateApiService;
import ru.rogovalex.translator.api.TranslateResponse;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.TranslateProvider;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:28
 */
public class YandexTranslateProvider implements TranslateProvider {

    private static final String API_KEY = "trnsl.1.1.20170401T123903Z.821a542db77c4103.831126fb02a869cdd86f9c876690e4c437f83f65";
    private static final String FORMAT_PLAIN = "plain";
    private static final String DISABLE_AUTO_DETERMINATION = "0";

    private final TranslateApiService mService;

    public YandexTranslateProvider(TranslateApiService service) {
        mService = service;
    }

    @Override
    public Observable<String> translate(final TranslateParams params) {
        return mService.translate(API_KEY, params.getText(),
                params.getTextLang() + "-" + params.getTranslationLang(),
                FORMAT_PLAIN, DISABLE_AUTO_DETERMINATION)
                .flatMap(new Function<TranslateResponse, Observable<String>>() {
                    @Override
                    public Observable<String> apply(TranslateResponse response) throws Exception {
                        if (response.getCode() != 200) {
                            return Observable.error(new ApiException(response.getCode()));
                        }
                        if (response.getText() == null || response.getText().length == 0) {
                            return Observable.error(new ApiException(422));
                        }
                        return Observable.just(response.getText()[0]);
                    }
                });
    }
}
