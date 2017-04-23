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
import ru.rogovalex.translator.data.LocalFavoriteRepository;
import ru.rogovalex.translator.data.LocalHistoryRepository;
import ru.rogovalex.translator.data.LocalLanguageModel;
import ru.rogovalex.translator.data.TranslationSharedPreferences;
import ru.rogovalex.translator.data.YandexDictionaryProvider;
import ru.rogovalex.translator.data.YandexTranslateProvider;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.data.database.DatabaseHelper;
import ru.rogovalex.translator.domain.DictionaryProvider;
import ru.rogovalex.translator.domain.TranslateProvider;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.history.HistoryRepository;
import ru.rogovalex.translator.domain.language.LanguageModel;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;

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
    public Database provideDatabase(SQLiteOpenHelper sqLiteOpenHelper) {
        return new Database(sqLiteOpenHelper);
    }

    @Provides
    @Singleton
    public FavoriteRepository provideFavoriteRepository(Database database) {
        return new LocalFavoriteRepository(database);
    }

    @Provides
    @Singleton
    public LanguageModel provideLanguageModel(Database database) {
        return new LocalLanguageModel(database);
    }

    @Provides
    @Singleton
    public HistoryRepository provideHistoryRepository(Database database) {
        return new LocalHistoryRepository(database);
    }

    @Provides
    @Singleton
    public TranslationPreferences provideTranslationPreferences(Context context) {
        return new TranslationSharedPreferences(context);
    }
}
