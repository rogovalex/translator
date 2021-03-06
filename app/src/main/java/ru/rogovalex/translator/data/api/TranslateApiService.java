package ru.rogovalex.translator.data.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.rogovalex.translator.data.api.response.LanguagesResponse;
import ru.rogovalex.translator.data.api.response.TranslateResponse;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:43
 */
public interface TranslateApiService {

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Observable<TranslateResponse> translate(
            @Field("key") String key,
            @Field("text") String text,
            @Field("lang") String lang,
            @Field("format") String format,
            @Field("options") String options);

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/getLangs")
    Observable<LanguagesResponse> languages(
            @Field("key") String key,
            @Field("ui") String uiLang);
}
