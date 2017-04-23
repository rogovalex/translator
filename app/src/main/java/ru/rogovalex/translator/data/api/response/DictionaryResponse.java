package ru.rogovalex.translator.data.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:51
 */
public class DictionaryResponse extends BaseResponse {

    @SerializedName("def")
    private DictionaryEntry[] entries;

    public DictionaryEntry[] getEntries() {
        return entries;
    }

    public void setEntries(DictionaryEntry[] entries) {
        this.entries = entries;
    }
}
