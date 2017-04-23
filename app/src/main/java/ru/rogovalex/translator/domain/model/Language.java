package ru.rogovalex.translator.domain.model;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 04.04.2017
 * Time: 23:54
 */
public class Language {

    private String mCode;
    private String mName;

    public Language(String code, String name) {
        mCode = code;
        mName = name;
    }

    public String getCode() {
        return mCode;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (mCode != null ? !mCode.equals(language.mCode) : language.mCode != null) return false;
        return mName != null ? mName.equals(language.mName) : language.mName == null;

    }

    @Override
    public int hashCode() {
        int result = mCode != null ? mCode.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        return result;
    }
}
