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

    @Override
    public int hashCode() {
        int result = (mText != null ? mText.hashCode() : 0);
        result = 31 * result + (mTextLang != null ? mTextLang.hashCode() : 0);
        result = 31 * result + (mTranslationLang != null ? mTranslationLang.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslateParams params = (TranslateParams) o;

        if (mText != null
                ? !mText.equals(params.mText)
                : params.mText != null)
            return false;
        if (mTextLang != null
                ? !mTextLang.equals(params.mTextLang)
                : params.mTextLang != null)
            return false;
        if (mTranslationLang != null
                ? !mTranslationLang.equals(params.mTranslationLang)
                : params.mTranslationLang != null)
            return false;

        return true;
    }
}
