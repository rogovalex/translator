package ru.rogovalex.translator.presentation.main.translate;

import javax.inject.Inject;

import io.reactivex.internal.functions.Functions;
import ru.rogovalex.translator.domain.favorite.UpdateFavoriteInteractor;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.domain.model.TranslationParams;
import ru.rogovalex.translator.domain.translate.TranslateInteractor;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:32
 */
public class TranslationViewPresenter extends BasePresenter<TranslationView> {

    private final TranslateInteractor mInteractor;
    private final UpdateFavoriteInteractor mUpdateInteractor;
    private final TranslationPreferences mTranslationPreferences;

    private String mText = "";
    private Language mSourceLang;
    private Language mTranslationLang;
    private String mUiLangCode;
    private Translation mResult;

    @Inject
    public TranslationViewPresenter(TranslateInteractor interactor,
                                    UpdateFavoriteInteractor updateInteractor,
                                    TranslationPreferences translationPreferences) {
        mInteractor = interactor;
        mUpdateInteractor = updateInteractor;
        mTranslationPreferences = translationPreferences;
        super.setView(sStubView);
    }

    @Override
    protected TranslationView getStubView() {
        return sStubView;
    }

    @Override
    public void setView(TranslationView view) {
        super.setView(view);

        getView().setLanguages(mSourceLang, mTranslationLang);

        if (mResult != null) {
            getView().onTranslated(mResult);
        }
        if (mInteractor.isRunning()) {
            getView().onTranslating();
        }
    }

    public void setUiLanguageCode(String uiLangCode) {
        if (!uiLangCode.equals(mUiLangCode)) {
            cancelTranslation();
            mUiLangCode = uiLangCode;
        }
    }

    public void updateLanguages() {
        Language sourceLang = mTranslationPreferences.getSourceLanguage();
        Language translationLang = mTranslationPreferences.getTranslationLanguage();

        if (sourceLang.equals(mSourceLang) && translationLang.equals(mTranslationLang)) {
            return;
        }
        if (sourceLang.equals(mTranslationLang) && translationLang.equals(mSourceLang)
                && mResult != null && mResult.getText().equals(mText)) {
            mText = mResult.getTranslation();
        }
        setLanguages(sourceLang, translationLang);
    }

    public void swapLanguages() {
        mTranslationPreferences.setLanguages(mTranslationLang, mSourceLang);
        if (mResult != null && mResult.getText().equals(mText)) {
            mText = mResult.getTranslation();
        }
        setLanguages(mTranslationLang, mSourceLang);
    }

    private void setLanguages(Language sourceLang, Language translationLang) {
        cancelTranslation();
        mSourceLang = sourceLang;
        mTranslationLang = translationLang;
        getView().onTranslationDirectionChanged(mText, mSourceLang, mTranslationLang);
        translate();
    }

    public void onTextChanged(String text) {
        mText = text;
        if (text.isEmpty()) {
            getView().onTextEmpty();
        } else {
            getView().onTextNotEmpty();
        }
    }

    public void translate() {
        if (mText.isEmpty()) {
            return;
        }

        getView().onTranslating();

        if (mInteractor.isRunning()) {
            return;
        }

        TranslationParams params = new TranslationParams(mText, mSourceLang.getCode(),
                mTranslationLang.getCode(), mUiLangCode);
        mInteractor.execute(params, translation -> {
            mResult = translation;
            getView().onTranslated(translation);
        }, e -> getView().onTranslateError(e));
    }

    public void updateFavorite(Translation item) {
        mUpdateInteractor.execute(item, Functions.emptyConsumer(),
                Functions.emptyConsumer());
    }

    public void cancelTranslation() {
        mResult = null;
        mInteractor.cancel();
    }

    public void cancel() {
        cancelTranslation();
        mUpdateInteractor.cancel();
    }

    private static TranslationView sStubView = new TranslationView() {
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
        public void onTranslationDirectionChanged(String text, Language source, Language translation) {
        }

        @Override
        public void onTextEmpty() {
        }

        @Override
        public void onTextNotEmpty() {
        }

        @Override
        public void setLanguages(Language source, Language translation) {
        }
    };
}
