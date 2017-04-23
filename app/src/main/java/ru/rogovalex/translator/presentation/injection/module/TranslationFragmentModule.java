package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.DictionaryProvider;
import ru.rogovalex.translator.domain.TranslateProvider;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.history.HistoryRepository;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;
import ru.rogovalex.translator.presentation.main.translate.TranslationViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class TranslationFragmentModule {

    @Provides
    @ActivityScope
    public TranslateInteractor provideTranslateInteractor(
            @Named(DomainModule.JOB) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            TranslateProvider translateProvider,
            DictionaryProvider dictionaryProvider,
            HistoryRepository historyRepository) {
        return new TranslateInteractor(jobScheduler, uiScheduler, translateProvider,
                dictionaryProvider, historyRepository);
    }

    @Provides
    @ActivityScope
    public UpdateFavoriteInteractor provideUpdateFavoriteInteractor(
            @Named(DomainModule.LOCAL) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            FavoriteRepository favoriteRepository) {
        return new UpdateFavoriteInteractor(jobScheduler, uiScheduler, favoriteRepository);
    }

    @Provides
    @ActivityScope
    public TranslationViewPresenter provideTranslateViewPresenter(
            TranslateInteractor interactor,
            UpdateFavoriteInteractor updateInteractor,
            TranslationPreferences translationPreferences) {
        return new TranslationViewPresenter(interactor, updateInteractor, translationPreferences);
    }
}
