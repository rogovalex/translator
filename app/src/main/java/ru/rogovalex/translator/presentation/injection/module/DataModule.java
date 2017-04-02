package ru.rogovalex.translator.presentation.injection.module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.rogovalex.translator.api.DictionaryApiService;
import ru.rogovalex.translator.api.TranslateApiService;
import ru.rogovalex.translator.data.translate.Database;
import ru.rogovalex.translator.data.translate.DatabaseHelper;
import ru.rogovalex.translator.data.translate.YandexDictionaryProvider;
import ru.rogovalex.translator.data.translate.YandexTranslateProvider;
import ru.rogovalex.translator.domain.translate.DictionaryProvider;
import ru.rogovalex.translator.domain.translate.Storage;
import ru.rogovalex.translator.domain.translate.TranslateProvider;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:57
 */
@Module
public class DataModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    public RxJava2CallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public TranslateApiService provideTranslateApiService(
            OkHttpClient client, RxJava2CallAdapterFactory callAdapterFactory,
            GsonConverterFactory converterFactory) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .callFactory(client)
                .baseUrl("https://translate.yandex.net")
                .build()
                .create(TranslateApiService.class);
    }

    @Provides
    @Singleton
    public TranslateProvider provideTranslateProvider(TranslateApiService apiService) {
        return new YandexTranslateProvider(apiService);
    }

    @Provides
    @Singleton
    public DictionaryApiService provideDictionaryApiService(
            OkHttpClient client, RxJava2CallAdapterFactory callAdapterFactory,
            GsonConverterFactory converterFactory) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .callFactory(client)
                .baseUrl("https://dictionary.yandex.net")
                .build()
                .create(DictionaryApiService.class);
    }

    @Provides
    @Singleton
    public DictionaryProvider provideDictionaryProvider(DictionaryApiService apiService) {
        return new YandexDictionaryProvider(apiService);
    }

    @Provides
    @Singleton
    public SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        return new DatabaseHelper(context);
    }

    @Provides
    @Singleton
    public Storage provideStorage(SQLiteOpenHelper sqLiteOpenHelper) {
        return new Database(sqLiteOpenHelper);
    }
}
