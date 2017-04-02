package ru.rogovalex.translator.domain.translate;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:30
 */
public class LoadFavoritesInteractor extends Interactor<List<TranslateResult>, Void> {

    private final Storage mStorage;

    @Inject
    public LoadFavoritesInteractor(@Named(DomainModule.LOCAL) Scheduler jobScheduler,
                                   @Named(DomainModule.UI) Scheduler uiScheduler,
                                   Storage storage) {
        super(jobScheduler, uiScheduler);
        mStorage = storage;
    }

    @Override
    protected Observable<List<TranslateResult>> buildObservable(Void params) {
        return Observable.fromCallable(new Callable<List<TranslateResult>>() {
            @Override
            public List<TranslateResult> call() throws Exception {
                return mStorage.getFavoriteTranslations();
            }
        });
    }
}
