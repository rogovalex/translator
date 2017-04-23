package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.favorite.FavoriteRepository;
import ru.rogovalex.translator.domain.favorite.LoadFavoriteInteractor;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;
import ru.rogovalex.translator.presentation.main.favorite.FavoriteViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class FavoriteFragmentModule {

    @Provides
    @ActivityScope
    public LoadFavoriteInteractor provideLoadFavoriteInteractor(
            @Named(DomainModule.LOCAL) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            FavoriteRepository favoriteRepository) {
        return new LoadFavoriteInteractor(jobScheduler, uiScheduler, favoriteRepository);
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
    public FavoriteViewPresenter provideFavoriteViewPresenter(
            LoadFavoriteInteractor interactor,
            UpdateFavoriteInteractor updateInteractor) {
        return new FavoriteViewPresenter(interactor, updateInteractor);
    }
}
