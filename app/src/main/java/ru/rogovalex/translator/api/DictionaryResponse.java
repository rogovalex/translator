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
    private Entry[] entries;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Entry[] getEntries() {
        return entries;
    }

    public void setEntries(Entry[] entries) {
        this.entries = entries;
    }
}
