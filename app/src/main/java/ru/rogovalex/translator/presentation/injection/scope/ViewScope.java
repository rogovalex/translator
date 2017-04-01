package ru.rogovalex.translator.presentation.injection.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 20.08.2016
 * Time: 22:38
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewScope {
}
