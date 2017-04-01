package ru.rogovalex.translator.data.translate;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.api.TranslateApiService;
import ru.rogovalex.translator.api.TranslateResponse;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.TranslateProvider;
import ru.rogovalex.translator.domain.translate.TranslateResult;

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

    @Inject
    public YandexTranslateProvider(TranslateApiService service) {
        mService = service;
    }

    @Override
    public Observable<TranslateResult> translate(final TranslateParams params) {
        return mService.translate(API_KEY, params.getText(),
                params.getTextLang() + "-" + params.getTranslationLang(),
                FORMAT_PLAIN, DISABLE_AUTO_DETERMINATION)
                .flatMap(new Function<TranslateResponse, Observable<TranslateResult>>() {
                    @Override
                    public Observable<TranslateResult> apply(TranslateResponse response) throws Exception {
                        if (response.getCode() != 200) {
                            return Observable.error(new ApiException(response.getCode()));
                        }
                        TranslateResult result = new TranslateResult(params.getText(),
                                params.getTextLang(),
                                response.getText()[0],
                                params.getTranslationLang());
                        return Observable.just(result);
                    }
                });
    }
}
