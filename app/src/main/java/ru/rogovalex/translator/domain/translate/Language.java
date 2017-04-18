package ru.rogovalex.translator.domain.translate;

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
}
