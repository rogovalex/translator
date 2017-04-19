package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.TranslateProvider;
import ru.rogovalex.translator.domain.language.LanguageModel;
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
    public LoadLanguagesInteractor provideLoadLanguagesInteractor(
            @Named(DomainModule.JOB) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            TranslateProvider translateProvider,
            LanguageModel languageModel) {
        return new LoadLanguagesInteractor(jobScheduler, uiScheduler,
                translateProvider, languageModel);
    }

    @Provides
    @ActivityScope
    public LanguagesViewPresenter provideLanguagesViewPresenter(
            LoadLanguagesInteractor interactor) {
        return new LanguagesViewPresenter(interactor);
    }
}
