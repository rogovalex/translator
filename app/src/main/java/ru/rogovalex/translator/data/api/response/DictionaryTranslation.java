package ru.rogovalex.translator.data.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 21:38
 */
public class DictionaryTranslation {

    private String text;
    private String pos;
    private String gen;
    private String asp;
    private String num;
    @SerializedName("syn")
    private DictionaryTranslation[] synonyms;
    @SerializedName("mean")
    private DictionaryTranslation[] meanings;
    @SerializedName("ex")
    private DictionaryTranslation[] examples;

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

    public DictionaryTranslation[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(DictionaryTranslation[] synonyms) {
        this.synonyms = synonyms;
    }

    public DictionaryTranslation[] getMeanings() {
        return meanings;
    }

    public void setMeanings(DictionaryTranslation[] meanings) {
        this.meanings = meanings;
    }

    public DictionaryTranslation[] getExamples() {
        return examples;
    }

    public void setExamples(DictionaryTranslation[] examples) {
        this.examples = examples;
    }
}
