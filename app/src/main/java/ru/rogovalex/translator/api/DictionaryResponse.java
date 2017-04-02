package ru.rogovalex.translator.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:51
 */
public class DictionaryResponse {

    private int code = 200;
    @SerializedName("def")
    private DictionaryEntry[] entries;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DictionaryEntry[] getEntries() {
        return entries;
    }

    public void setEntries(DictionaryEntry[] entries) {
        this.entries = entries;
    }
}
