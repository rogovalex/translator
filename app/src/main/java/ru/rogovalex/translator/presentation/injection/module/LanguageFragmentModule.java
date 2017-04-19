package ru.rogovalex.translator.presentation.injection.module;

import dagger.Module;
import dagger.Provides;
import ru.rogovalex.translator.domain.language.LoadLanguagesInteractor;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;
import ru.rogovalex.translator.presentation.language.LanguagesViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class LanguageFragmentModule {

    @Provides
    @ActivityScope
    public LanguagesViewPresenter provideLanguagesViewPresenter(
            LoadLanguagesInteractor interactor) {
        return new LanguagesViewPresenter(interactor);
    }
}
