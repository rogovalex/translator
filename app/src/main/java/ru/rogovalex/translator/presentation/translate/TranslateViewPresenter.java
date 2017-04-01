package ru.rogovalex.translator.presentation.translate;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:32
 */
public class TranslateViewPresenter extends BasePresenter<TranslateView> {

    private final TranslateInteractor mInteractor;

    private TranslateParams mParams;

    private boolean mLoading;

    @Inject
    public TranslateViewPresenter(TranslateInteractor interactor) {
        mInteractor = interactor;
        setView(sStubView);
    }

    @Override
    public void setView(TranslateView view) {
        if (view == null) {
            view = sStubView;
        }
        super.setView(view);
    }

    public void translate(TranslateParams params) {
        getView().onTranslating();

        if (mLoading) {
            if (!params.equals(mParams)) {
                mInteractor.cancel();
            } else {
                return;
            }
        }

        mParams = params;
        mLoading = true;
        mInteractor.execute(mParams, new Consumer<TranslateResult>() {
            @Override
            public void accept(TranslateResult translation) throws Exception {
                mLoading = false;
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
