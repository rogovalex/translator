package ru.rogovalex.translator.presentation.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.rogovalex.translator.App;
import ru.rogovalex.translator.presentation.injection.component.AppComponent;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 11:23
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String COMPONENT_HOLDER = "component_holder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            addComponentHolder();
        }
    }

    private void addComponentHolder() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(COMPONENT_HOLDER);
        if (fragment == null) {
            fragment = new ComponentHolderFragment();
            fm.beginTransaction()
                    .add(fragment, COMPONENT_HOLDER)
                    .commit();
        }
    }

    private ComponentHolderFragment getComponentHolder() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(COMPONENT_HOLDER);
        return ((ComponentHolderFragment) fragment);
    }

    protected <T> T getComponent(String key, Class<T> cls, ComponentFactory<T> factory) {
        return getComponentHolder().getComponent(key, cls, factory);
    }

    protected AppComponent getAppComponent() {
        return ((App) getApplicationContext()).getAppComponent();
    }
}
