package ru.rogovalex.translator.presentation.injection.module;

import dagger.Module;
import dagger.Provides;
import ru.rogovalex.translator.domain.language.LoadLanguagesInteractor;
import ru.rogovalex.translator.presentation.injection.scope.ViewScope;
import ru.rogovalex.translator.presentation.translate.LanguagesViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 13:07
 */

@Module
public class LanguagesFragmentModule {

    @Provides
    @ViewScope
    public LanguagesViewPresenter provideLanguagesViewPresenter(
            LoadLanguagesInteractor interactor) {
        return new LanguagesViewPresenter(interactor);
    }
}
