package ru.rogovalex.translator.presentation.injection.module;

import dagger.Module;
import dagger.Provides;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;
import ru.rogovalex.translator.presentation.main.translate.TranslateViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class TranslateFragmentModule {

    @Provides
    @ActivityScope
    public TranslateViewPresenter provideTranslateViewPresenter(
            TranslateInteractor interactor,
            UpdateFavoriteInteractor updateInteractor) {
        return new TranslateViewPresenter(interactor, updateInteractor);
    }
}
