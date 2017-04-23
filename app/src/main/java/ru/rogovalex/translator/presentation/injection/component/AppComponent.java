package ru.rogovalex.translator.presentation.injection.component;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.DictionaryProvider;
import ru.rogovalex.translator.domain.TranslateProvider;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.history.HistoryModel;
import ru.rogovalex.translator.domain.language.LanguageModel;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;
import ru.rogovalex.translator.presentation.injection.module.AppModule;
import ru.rogovalex.translator.presentation.injection.module.DataModule;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;
import ru.rogovalex.translator.presentation.language.LanguageActivity;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:04
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class, DomainModule.class})
public interface AppComponent {

    Context context();

    @Named(DomainModule.JOB)
    Scheduler jobScheduler();

    @Named(DomainModule.UI)
    Scheduler uiScheduler();

    @Named(DomainModule.LOCAL)
    Scheduler localScheduler();

    TranslateProvider translateProvider();

    DictionaryProvider dictionaryProvider();

    FavoriteRepository favoriteRepository();

    LanguageModel languageModel();

    HistoryModel historyModel();

    TranslationPreferences translationPreferences();

    void inject(LanguageActivity activity);
}
