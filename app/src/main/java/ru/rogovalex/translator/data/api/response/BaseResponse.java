package ru.rogovalex.translator.data.api.response;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 10:47
 */
public class BaseResponse {

    private int code = 200;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
