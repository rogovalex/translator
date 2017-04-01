package ru.rogovalex.translator.presentation.injection.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:05
 */
@Module
public class DomainModule {

    public static final String JOB = "JOB";
    public static final String UI = "UI";

    @Provides
    @Singleton
    @Named(JOB)
    public Scheduler provideJobScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named(UI)
    public Scheduler provideUiScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
