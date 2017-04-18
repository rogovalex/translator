package ru.rogovalex.translator.presentation.injection.module;

import dagger.Module;
import dagger.Provides;
import ru.rogovalex.translator.domain.translate.LoadFavoritesInteractor;
import ru.rogovalex.translator.domain.translate.UpdateFavoriteInteractor;
import ru.rogovalex.translator.presentation.injection.scope.ViewScope;
import ru.rogovalex.translator.presentation.translate.FavoriteViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class FavoriteFragmentModule {

    @Provides
    @ViewScope
    public FavoriteViewPresenter provideFavoriteViewPresenter(
            LoadFavoritesInteractor interactor,
            UpdateFavoriteInteractor updateInteractor) {
        return new FavoriteViewPresenter(interactor, updateInteractor);
    }
}
