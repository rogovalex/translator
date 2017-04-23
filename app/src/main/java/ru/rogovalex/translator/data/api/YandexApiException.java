package ru.rogovalex.translator.data.api;

import java.util.HashMap;

import ru.rogovalex.translator.domain.model.ApiException;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:42
 */
public class YandexApiException extends ApiException {

    private static final HashMap<Integer, String> mCodes =
            new HashMap<Integer, String>() {{
                put(401, "Неправильный API-ключ");
                put(402, "API-ключ заблокирован");
                put(404, "Превышено суточное ограничение на объем переведенного текста");
                put(413, "Превышен максимально допустимый размер текста");
                put(422, "Текст не может быть переведен");
                put(501, "Заданное направление перевода не поддерживается");
            }};

    private int mCode;

    public YandexApiException(int code) {
        super(mCodes.get(code));
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}
