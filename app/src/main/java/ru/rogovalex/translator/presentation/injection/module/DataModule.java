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
import ru.rogovalex.translator.data.CachingLanguagesRepository;
import ru.rogovalex.translator.data.CachingTranslationRepository;
import ru.rogovalex.translator.data.DictionaryProvider;
import ru.rogovalex.translator.data.LocalFavoriteRepository;
import ru.rogovalex.translator.data.LocalHistoryRepository;
import ru.rogovalex.translator.data.TranslationProvider;
import ru.rogovalex.translator.data.TranslationSharedPreferences;
import ru.rogovalex.translator.data.YandexDictionaryProvider;
import ru.rogovalex.translator.data.YandexTranslationProvider;
import ru.rogovalex.translator.data.api.DictionaryApiService;
import ru.rogovalex.translator.data.api.TranslateApiService;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.data.database.DatabaseHelper;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.history.HistoryRepository;
import ru.rogovalex.translator.domain.language.LanguagesRepository;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;
import ru.rogovalex.translator.domain.translate.TranslationRepository;

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
    public TranslationProvider provideTranslationProvider(TranslateApiService apiService) {
        return new YandexTranslationProvider(apiService);
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
    public TranslationRepository provideTranslationRepository(
            Database database, TranslationProvider translateProvider,
            DictionaryProvider dictionaryProvider) {
        return new CachingTranslationRepository(database, translateProvider,
                dictionaryProvider);
    }

    @Provides
    @Singleton
    public FavoriteRepository provideFavoriteRepository(Database database) {
        return new LocalFavoriteRepository(database);
    }

    @Provides
    @Singleton
    public LanguagesRepository provideLanguagesRepository(
            Database database, TranslationProvider provider) {
        return new CachingLanguagesRepository(database, provider);
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
