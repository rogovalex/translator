package ru.rogovalex.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:59
 */
public class ComponentHolderFragment extends Fragment {

    private HashMap<String, Object> mComponents = new HashMap<>();
    private HashMap<String, Class> mClasses = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public <T> T getComponent(String key, Class<T> clazz, ComponentFactory<T> factory) {
        Object component = mComponents.get(key);
        if (component == null || !(mClasses.get(key).equals(clazz))) {
            component = factory.buildComponent();
            mComponents.put(key, component);
            mClasses.put(key, clazz);
        }
        return clazz.cast(component);
    }

    public void removeComponent(String key) {
        mComponents.remove(key);
        mClasses.remove(key);
    }
}
