package ru.rogovalex.translator.presentation.injection.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 17:56
 */
@Module
public class AppModule {

    private final Context mAppContext;

    public AppModule(Context context) {
        mAppContext = context;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mAppContext;
    }
}
