package ru.rogovalex.translator.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:43
 */
public interface DictionaryApiService {

    @FormUrlEncoded
    @POST("/api/v1/dicservice.json/lookup")
    Observable<DictionaryResponse> lookup(
            @Field("key") String key,
            @Field("text") String text,
            @Field("lang") String lang,
            @Field("ui") String ui,
            @Field("flags") int flags);
}
