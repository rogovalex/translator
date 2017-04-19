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

    private final static String CHANGE_SOURCE_LANG = "LanguagesActivity.source";

    public static Intent newIntent(Context packageContext, boolean changeSourceLanguage) {
        Intent intent = new Intent(packageContext, LanguagesActivity.class);
        intent.putExtra(CHANGE_SOURCE_LANG, changeSourceLanguage);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        if (savedInstanceState == null) {
            addLanguagesFragment();
        }

        boolean source = getIntent().getBooleanExtra(CHANGE_SOURCE_LANG, false);
        setTitle(source ? R.string.title_source_lang : R.string.title_translation_lang);

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
        if (getIntent().getBooleanExtra(CHANGE_SOURCE_LANG, false)) {
            PreferencesHelper.setSourceLanguage(this, item);
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
