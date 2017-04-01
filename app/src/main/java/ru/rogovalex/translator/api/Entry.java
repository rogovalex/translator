package ru.rogovalex.translator.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 21:36
 */
public class Entry {

    private String text;
    private String pos;
    @SerializedName("ts")
    private String transcription;
    @SerializedName("tr")
    private Translation[] translations;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public Translation[] getTranslations() {
        return translations;
    }

    public void setTranslations(Translation[] translations) {
        this.translations = translations;
    }
}
