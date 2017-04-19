package ru.rogovalex.translator.domain.model;

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
    private List<DefinitionOption> mDefinitionOptions;

    public Definition(String text, String transcription, String pos, List<DefinitionOption> definitionOptions) {
        mText = text;
        mTranscription = transcription;
        mPos = pos;
        mDefinitionOptions = definitionOptions;
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

    public List<DefinitionOption> getDefinitionOptions() {
        return mDefinitionOptions;
    }
}
