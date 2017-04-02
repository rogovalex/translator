package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.translate.DictionaryProvider;
import ru.rogovalex.translator.domain.translate.Storage;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
import ru.rogovalex.translator.domain.translate.TranslateProvider;
import ru.rogovalex.translator.presentation.injection.scope.ViewScope;
import ru.rogovalex.translator.presentation.translate.TranslateViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:43
 */
@Module
public class TranslatePresentationModule {

    @Provides
    @ViewScope
    public TranslateInteractor provideTranslateInteractor(
            @Named(DomainModule.JOB) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            TranslateProvider translateProvider,
            DictionaryProvider dictionaryProvider,
            Storage storage) {
        return new TranslateInteractor(jobScheduler, uiScheduler,
                translateProvider, dictionaryProvider, storage);
    }

    @Provides
    @ViewScope
    public TranslateViewPresenter provideTranslateViewPresenter(
            TranslateInteractor translateInteractor) {
        return new TranslateViewPresenter(translateInteractor);
    }
}
