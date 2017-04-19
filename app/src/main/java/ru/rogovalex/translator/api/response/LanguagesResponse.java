package ru.rogovalex.translator.api.response;

import java.util.Map;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:51
 */
public class LanguagesResponse extends BaseResponse {

    private Map<String, String> langs;

    public Map<String, String> getLangs() {
        return langs;
    }

    public void setLangs(Map<String, String> langs) {
        this.langs = langs;
    }
}
