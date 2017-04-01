package ru.rogovalex.translator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

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
            case R.id.navigation_settings:
                showFragment(SettingsFragment.class);
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
