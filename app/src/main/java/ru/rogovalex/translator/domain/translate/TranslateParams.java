package ru.rogovalex.translator.domain.translate;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:22
 */
public class TranslateParams {

    private String mText;
    private String mTextLang;
    private String mTranslationLang;

    public TranslateParams(String text, String textLang, String translationLang) {
        mText = text;
        mTextLang = textLang;
        mTranslationLang = translationLang;
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
}
