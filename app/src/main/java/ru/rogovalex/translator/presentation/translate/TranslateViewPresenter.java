package ru.rogovalex.translator.presentation.translate;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.domain.translate.UpdateFavoriteInteractor;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:32
 */
public class TranslateViewPresenter extends BasePresenter<TranslateView> {

    private final TranslateInteractor mInteractor;
    private final UpdateFavoriteInteractor mUpdateInteractor;

    private TranslateParams mParams;
    private TranslateResult mResult;

    private boolean mLoading;

    @Inject
    public TranslateViewPresenter(TranslateInteractor interactor,
                                  UpdateFavoriteInteractor updateInteractor) {
        mInteractor = interactor;
        mUpdateInteractor = updateInteractor;
        super.setView(sStubView);
    }

    @Override
    public void setView(TranslateView view) {
        if (view == null) {
            view = sStubView;
        }
        super.setView(view);
    }

    public void translate(TranslateParams params) {
        if (params.getText().isEmpty()) {
            cancel();
            return;
        }

        getView().onTranslating();

        if (mLoading) {
            if (!params.equals(mParams)) {
                mInteractor.cancel();
            } else {
                return;
            }
        } else if (params.equals(mParams) && mResult != null) {
            getView().onTranslated(mResult);
            return;
        }

        mParams = params;
        mResult = null;
        mLoading = true;
        mInteractor.execute(mParams, new Consumer<TranslateResult>() {
            @Override
            public void accept(TranslateResult translation) throws Exception {
                mLoading = false;
                mResult = translation;
                getView().onTranslated(translation);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mLoading = false;
                getView().onTranslateError(throwable);
            }
        });
    }

    public void updateFavorite(TranslateResult item) {
        mUpdateInteractor.execute(item, Functions.<Boolean>emptyConsumer());
    }

    public void cancel() {
        mLoading = false;
        mInteractor.cancel();
        mUpdateInteractor.cancel();
    }

    private static TranslateView sStubView = new TranslateView() {
        @Override
        public void onTranslating() {
        }

        @Override
        public void onTranslated(TranslateResult translation) {
        }

        @Override
        public void onTranslateError(Throwable e) {
        }
    };
}
