package ru.rogovalex.translator;

import android.app.Application;

import ru.rogovalex.translator.presentation.injection.component.AppComponent;
import ru.rogovalex.translator.presentation.injection.component.DaggerAppComponent;
import ru.rogovalex.translator.presentation.injection.module.AppModule;
import ru.rogovalex.translator.presentation.injection.module.DataModule;
import ru.rogovalex.translator.presentation.injection.module.DomainModule;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:02
 */
public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .domainModule(new DomainModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
