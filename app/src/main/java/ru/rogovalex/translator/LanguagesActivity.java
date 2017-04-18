package ru.rogovalex.translator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import ru.rogovalex.translator.domain.translate.Language;
import ru.rogovalex.translator.presentation.injection.component.DaggerLanguagesFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.LanguagesFragmentComponent;

public class LanguagesActivity extends BaseActivity
        implements LanguagesFragment.Callbacks {

    private final static String ORIGIN = "LanguagesActivity.origin";

    public static Intent newIntent(Context packageContext, boolean origin) {
        Intent intent = new Intent(packageContext, LanguagesActivity.class);
        intent.putExtra(ORIGIN, origin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        if (savedInstanceState == null) {
            addLanguagesFragment();
        }

        boolean origin = getIntent().getBooleanExtra(ORIGIN, false);
        setTitle(origin ? R.string.title_origin_lang : R.string.title_translation_lang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public LanguagesFragmentComponent getLanguagesFragmentComponent() {
        return getComponent("main", LanguagesFragmentComponent.class,
                new ComponentFactory<LanguagesFragmentComponent>() {
                    @Override
                    public LanguagesFragmentComponent buildComponent() {
                        return DaggerLanguagesFragmentComponent.builder()
                                .appComponent(getAppComponent())
                                .build();
                    }
                });
    }

    @Override
    public void onLanguageSelected(Language item) {
        if (getIntent().getBooleanExtra(ORIGIN, false)) {
            PreferencesHelper.setOriginLanguage(this, item);
        } else {
            PreferencesHelper.setTranslationLanguage(this, item);
        }
        setResult(RESULT_OK);
        supportFinishAfterTransition();
    }

    private void addLanguagesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {

            fragment = new LanguagesFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
