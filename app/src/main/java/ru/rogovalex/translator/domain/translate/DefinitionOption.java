package ru.rogovalex.translator.domain.translate;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 11:28
 */
public class DefinitionOption {

    private String mSynonyms;
    private String mMeanings;
    private String mExamples;

    public DefinitionOption(String synonyms, String meanings, String examples) {
        mSynonyms = synonyms;
        mMeanings = meanings;
        mExamples = examples;
    }

    public String getSynonyms() {
        return mSynonyms;
    }

    public String getMeanings() {
        return mMeanings;
    }

    public String getExamples() {
        return mExamples;
    }
}
