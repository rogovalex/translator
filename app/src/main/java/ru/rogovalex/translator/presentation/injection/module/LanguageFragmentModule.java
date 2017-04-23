package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.language.LanguagesRepository;
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
            LanguagesRepository languagesRepository) {
        return new LoadLanguagesInteractor(jobScheduler, uiScheduler,
                languagesRepository);
    }

    @Provides
    @ActivityScope
    public LanguagesViewPresenter provideLanguagesViewPresenter(
            LoadLanguagesInteractor interactor) {
        return new LanguagesViewPresenter(interactor);
    }
}
