package ru.rogovalex.translator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import ru.rogovalex.translator.presentation.injection.component.DaggerFavoriteFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.DaggerHistoryFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.DaggerTranslateFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.FavoriteFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.HistoryFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.TranslateFragmentComponent;

public class MainActivity extends BaseActivity
        implements TranslateFragment.Callbacks,
        HistoryFragment.Callbacks,
        FavoriteFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showFragment(item.getItemId());
                return true;
            }
        });

        if (savedInstanceState == null) {
            showFragment(navigation.getSelectedItemId());
        }
    }

    @Override
    public TranslateFragmentComponent getTranslateFragmentComponent() {
        return getComponent("main", TranslateFragmentComponent.class,
                new ComponentFactory<TranslateFragmentComponent>() {
                    @Override
                    public TranslateFragmentComponent buildComponent() {
                        return DaggerTranslateFragmentComponent.builder()
                                .appComponent(getAppComponent())
                                .build();
                    }
                });
    }

    @Override
    public HistoryFragmentComponent getHistoryFragmentComponent() {
        return getComponent("main", HistoryFragmentComponent.class,
                new ComponentFactory<HistoryFragmentComponent>() {
                    @Override
                    public HistoryFragmentComponent buildComponent() {
                        return DaggerHistoryFragmentComponent.builder()
                                .appComponent(getAppComponent())
                                .build();
                    }
                });
    }

    @Override
    public FavoriteFragmentComponent getFavoriteFragmentComponent() {
        return getComponent("main", FavoriteFragmentComponent.class,
                new ComponentFactory<FavoriteFragmentComponent>() {
                    @Override
                    public FavoriteFragmentComponent buildComponent() {
                        return DaggerFavoriteFragmentComponent.builder()
                                .appComponent(getAppComponent())
                                .build();
                    }
                });
    }

    private void showFragment(int id) {
        switch (id) {
            case R.id.navigation_translate:
                showFragment(TranslateFragment.class);
                break;
            case R.id.navigation_history:
                showFragment(HistoryFragment.class);
                break;
            case R.id.navigation_favorite:
                showFragment(FavoriteFragment.class);
                break;
        }
    }

    private void showFragment(@NonNull Class<? extends Fragment> cls) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null || !fragment.getClass().equals(cls)) {
            fragment = Fragment.instantiate(this, cls.getName());
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
