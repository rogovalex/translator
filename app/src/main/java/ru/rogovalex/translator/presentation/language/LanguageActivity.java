package ru.rogovalex.translator.presentation.language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import javax.inject.Inject;

import ru.rogovalex.translator.R;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;
import ru.rogovalex.translator.presentation.common.BaseActivity;
import ru.rogovalex.translator.presentation.injection.component.DaggerLanguageFragmentComponent;
import ru.rogovalex.translator.presentation.injection.component.LanguageFragmentComponent;

public class LanguageActivity extends BaseActivity
        implements LanguageFragment.Callbacks {

    private final static String CHANGE_SOURCE_LANG = "LanguageActivity.source";

    @Inject
    TranslationPreferences mTranslationPreferences;

    public static Intent newIntent(Context packageContext, boolean changeSourceLanguage) {
        Intent intent = new Intent(packageContext, LanguageActivity.class);
        intent.putExtra(CHANGE_SOURCE_LANG, changeSourceLanguage);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getAppComponent().inject(this);

        boolean source = getIntent().getBooleanExtra(CHANGE_SOURCE_LANG, false);
        setTitle(source ? R.string.title_source_lang : R.string.title_translation_lang);

        if (savedInstanceState == null) {
            addLanguagesFragment(source);
        }

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
    public LanguageFragmentComponent getLanguagesFragmentComponent() {
        return getComponent("lang", LanguageFragmentComponent.class,
                () -> DaggerLanguageFragmentComponent.builder()
                        .appComponent(getAppComponent())
                        .build());
    }

    @Override
    public void onLanguageSelected(Language item) {
        if (getIntent().getBooleanExtra(CHANGE_SOURCE_LANG, false)) {
            mTranslationPreferences.setSourceLanguage(item);
        } else {
            mTranslationPreferences.setTranslationLanguage(item);
        }
        setResult(RESULT_OK);
        supportFinishAfterTransition();
    }

    private void addLanguagesFragment(boolean source) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            Language language = source ? mTranslationPreferences.getSourceLanguage()
                    : mTranslationPreferences.getTranslationLanguage();
            fragment = LanguageFragment.newInstance(language);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
