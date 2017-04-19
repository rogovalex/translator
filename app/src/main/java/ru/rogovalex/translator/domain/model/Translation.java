package ru.rogovalex.translator.domain.model;

import java.util.List;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:22
 */
public class Translation {

    private String mText;
    private String mTextLang;
    private String mTranslation;
    private String mTranslationLang;
    private boolean mFavorite;
    private List<Definition> mDefinitions;

    public Translation(String text, String textLang, String translation,
                       String translationLang, List<Definition> definitions) {
        mText = text;
        mTextLang = textLang;
        mTranslation = translation;
        mTranslationLang = translationLang;
        mDefinitions = definitions;
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

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public List<Definition> getDefinitions() {
        return mDefinitions;
    }
}
