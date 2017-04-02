package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.translate.HistoryInteractor;
import ru.rogovalex.translator.domain.translate.Storage;
import ru.rogovalex.translator.presentation.injection.scope.ViewScope;
import ru.rogovalex.translator.presentation.translate.HistoryViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:43
 */
@Module
public class HistoryPresentationModule {

    @Provides
    @ViewScope
    public HistoryInteractor provideHistoryInteractor(
            @Named(DomainModule.LOCAL) Scheduler jobScheduler,
            @Named(DomainModule.UI) Scheduler uiScheduler,
            Storage storage) {
        return new HistoryInteractor(jobScheduler, uiScheduler, storage);
    }

    @Provides
    @ViewScope
    public HistoryViewPresenter provideHistoryViewPresenter(
            HistoryInteractor interactor) {
        return new HistoryViewPresenter(interactor);
    }
}
