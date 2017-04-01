package ru.rogovalex.translator.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 21:38
 */
public class Translation {

    private String text;
    private String pos;
    private String gen;
    private String asp;
    private String num;
    @SerializedName("syn")
    private Translation[] synonyms;
    @SerializedName("mean")
    private Translation[] meanings;
    @SerializedName("ex")
    private Translation[] examples;

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

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Translation[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Translation[] synonyms) {
        this.synonyms = synonyms;
    }

    public Translation[] getMeanings() {
        return meanings;
    }

    public void setMeanings(Translation[] meanings) {
        this.meanings = meanings;
    }

    public Translation[] getExamples() {
        return examples;
    }

    public void setExamples(Translation[] examples) {
        this.examples = examples;
    }
}
