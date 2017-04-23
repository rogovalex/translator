package ru.rogovalex.translator.domain.favorite;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import ru.rogovalex.translator.domain.common.Interactor;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:30
 */
public class UpdateFavoriteInteractor extends Interactor<Boolean, Translation> {

    private final FavoriteRepository mRepository;

    @Inject
    public UpdateFavoriteInteractor(@Named(DomainModule.LOCAL) Scheduler jobScheduler,
                                    @Named(DomainModule.UI) Scheduler uiScheduler,
                                    FavoriteRepository repository) {
        super(jobScheduler, uiScheduler);
        mRepository = repository;
    }

    @Override
    protected Observable<Boolean> buildObservable(final Translation params) {
        return mRepository.updateFavorite(params);
    }
}
