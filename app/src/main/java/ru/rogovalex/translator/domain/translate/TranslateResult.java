package ru.rogovalex.translator.domain.translate;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:22
 */
public class TranslateResult {

    private String mText;
    private String mTextLang;
    private String mTranslation;
    private String mTranslationLang;

    public TranslateResult(String text, String textLang, String translation,
                           String translationLang) {
        mText = text;
        mTextLang = textLang;
        mTranslation = translation;
        mTranslationLang = translationLang;
    }

    public String getText() {
        return mText;
    }

    public String getTextLang() {
        return mTextLang;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public String getTranslationLang() {
        return mTranslationLang;
    }
}
