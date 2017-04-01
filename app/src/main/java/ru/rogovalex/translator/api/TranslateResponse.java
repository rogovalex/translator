package ru.rogovalex.translator.api;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:51
 */
public class TranslateResponse {

    private int code;
    private String lang;
    private String[] text;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }
}
