package ru.rogovalex.translator;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 20:00
 */
public interface ComponentFactory<T> {
    T buildComponent();
}
