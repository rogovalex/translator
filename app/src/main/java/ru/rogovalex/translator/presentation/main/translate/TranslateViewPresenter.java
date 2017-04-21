package ru.rogovalex.translator.presentation.main.translate;

import javax.inject.Inject;

import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.model.TranslateParams;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
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

    private String mSource;
    private String mTranslation;
    private String mUiLangCode;
    private Translation mResult;

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

        if (mResult != null) {
            getView().onTranslated(mResult);
        }
        if (mLoading) {
            getView().onTranslating();
        }
    }

    public void setUiLanguageCode(String uiLangCode) {
        if (!uiLangCode.equals(mUiLangCode)) {
            cancelTranslate();
            mUiLangCode = uiLangCode;
        }
    }

    public void setTranslationDirection(String text, String source, String translation) {
        if (source.equals(mSource) && translation.equals(mTranslation)) {
            if (mResult != null && !mResult.getText().equals(text)) {
                cancelTranslate();
            }
            return;
        }
        if (source.equals(mTranslation) && translation.equals(mSource)
                && mResult != null && mResult.getText().equals(text)) {
            text = mResult.getTranslation();
        }
        getView().onTranslationDirectionChanged(text);
        cancelTranslate();
        mSource = source;
        mTranslation = translation;
        translate(text);
    }

    public void translate(String text) {
        if (text.isEmpty()) {
            return;
        }

        getView().onTranslating();

        mLoading = true;
        TranslateParams params = new TranslateParams(text, mSource, mTranslation, mUiLangCode);
        mInteractor.execute(params, translation -> {
            mLoading = false;
            mResult = translation;
            getView().onTranslated(translation);
        }, throwable -> {
            mLoading = false;
            getView().onTranslateError(throwable);
        });
    }

    public void updateFavorite(Translation item) {
        mUpdateInteractor.execute(item, Functions.emptyConsumer(),
                Functions.emptyConsumer());
    }

    public void cancelTranslate() {
        mResult = null;
        mLoading = false;
        mInteractor.cancel();
    }

    public void cancel() {
        cancelTranslate();
        mUpdateInteractor.cancel();
    }

    private static TranslateView sStubView = new TranslateView() {
        @Override
        public void onTranslating() {
        }

        @Override
        public void onTranslated(Translation translation) {
        }

        @Override
        public void onTranslateError(Throwable e) {
        }

        @Override
        public void onTranslationDirectionChanged(String text) {
        }
    };
}
