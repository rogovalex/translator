package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.history.ClearHistoryInteractor;
import ru.rogovalex.translator.domain.history.HistoryRepository;
import ru.rogovalex.translator.domain.history.LoadHistoryInteractor;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;
import ru.rogovalex.translator.presentation.main.history.HistoryViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class HistoryFragmentModule {

    @Provides
    @ActivityScope
    public LoadHistoryInteractor provideLoadHistoryInteractor(
            @Named(DomainModule.LOCAL) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            HistoryRepository historyRepository) {
        return new LoadHistoryInteractor(jobScheduler, uiScheduler, historyRepository);
    }

    @Provides
    @ActivityScope
    public ClearHistoryInteractor provideClearHistoryInteractor(
            @Named(DomainModule.LOCAL) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            HistoryRepository historyRepository) {
        return new ClearHistoryInteractor(jobScheduler, uiScheduler, historyRepository);
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
    public HistoryViewPresenter provideHistoryViewPresenter(
            LoadHistoryInteractor interactor,
            ClearHistoryInteractor clearInteractor,
            UpdateFavoriteInteractor updateInteractor) {
        return new HistoryViewPresenter(interactor, clearInteractor, updateInteractor);
    }
}
