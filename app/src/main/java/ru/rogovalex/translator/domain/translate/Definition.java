package ru.rogovalex.translator.domain.translate;

import java.util.List;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 11:27
 */
public class Definition {

    private String mText;
    private String mTranscription;
    private String mPos;
    private List<Translation> mTranslations;

    public Definition(String text, String transcription, String pos, List<Translation> translations) {
        mText = text;
        mTranscription = transcription;
        mPos = pos;
        mTranslations = translations;
    }

    public String getText() {
        return mText;
    }

    public String getTranscription() {
        return mTranscription;
    }

    public String getPos() {
        return mPos;
    }

    public List<Translation> getTranslations() {
        return mTranslations;
    }
}
