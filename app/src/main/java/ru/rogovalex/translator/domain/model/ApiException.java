package ru.rogovalex.translator.domain.model;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:42
 */
public abstract class ApiException extends Exception {

    public ApiException(String message) {
        super(message);
    }
}
