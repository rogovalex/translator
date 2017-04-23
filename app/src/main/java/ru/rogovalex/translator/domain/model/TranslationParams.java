package ru.rogovalex.translator.domain.model;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:22
 */
public class TranslationParams {

    private String mText;
    private String mTextLang;
    private String mTranslationLang;
    private String mUiLangCode;

    public TranslationParams(String text, String textLang, String translationLang, String uiLangCode) {
        mText = text;
        mTextLang = textLang;
        mTranslationLang = translationLang;
        mUiLangCode = uiLangCode;
    }

    public String getText() {
        return mText;
    }

    public String getTextLang() {
        return mTextLang;
    }

    public String getTranslationLang() {
        return mTranslationLang;
    }

    public String getUiLangCode() {
        return mUiLangCode;
    }
}
