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

    private final FavoriteModel mModel;

    @Inject
    public UpdateFavoriteInteractor(@Named(DomainModule.LOCAL) Scheduler jobScheduler,
                                    @Named(DomainModule.UI) Scheduler uiScheduler,
                                    FavoriteModel model) {
        super(jobScheduler, uiScheduler);
        mModel = model;
    }

    @Override
    protected Observable<Boolean> buildObservable(final Translation params) {
        return mModel.updateFavorite(params);
    }
}
