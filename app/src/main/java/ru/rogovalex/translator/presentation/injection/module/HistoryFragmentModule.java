package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.favorite.FavoriteModel;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.history.HistoryModel;
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
            HistoryModel historyModel) {
        return new LoadHistoryInteractor(jobScheduler, uiScheduler, historyModel);
    }

    @Provides
    @ActivityScope
    public UpdateFavoriteInteractor provideUpdateFavoriteInteractor(
            @Named(DomainModule.LOCAL) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            FavoriteModel favoriteModel) {
        return new UpdateFavoriteInteractor(jobScheduler, uiScheduler, favoriteModel);
    }

    @Provides
    @ActivityScope
    public HistoryViewPresenter provideHistoryViewPresenter(
            LoadHistoryInteractor interactor,
            UpdateFavoriteInteractor updateInteractor) {
        return new HistoryViewPresenter(interactor, updateInteractor);
    }
}
