package ru.rogovalex.translator.data;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import ru.rogovalex.translator.R;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.translate.TranslationPreferences;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 16:19
 */
public class TranslationSharedPreferences implements TranslationPreferences {

    private static final String PREFERENCES = "ru.rogovalex.translator.preference";

    private static final String SOURCE_NAME = "sourceLangName";
    private static final String SOURCE_CODE = "sourceLangCode";
    private static final String TRANSLATION_NAME = "translationLangName";
    private static final String TRANSLATION_CODE = "translationLangCode";

    private final Context mContext;
    private final SharedPreferences mPreferences;

    @Inject
    public TranslationSharedPreferences(Context context) {
        mContext = context;
        mPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public Language getSourceLanguage() {
        String code = mPreferences.getString(SOURCE_CODE,
                mContext.getString(R.string.source_code_default));
        String name = mPreferences.getString(SOURCE_NAME,
                mContext.getString(R.string.source_name_default));
        return new Language(code, name);
    }

    @Override
    public Language getTranslationLanguage() {
        String code = mPreferences.getString(TRANSLATION_CODE,
                mContext.getString(R.string.translation_code_default));
        String name = mPreferences.getString(TRANSLATION_NAME,
                mContext.getString(R.string.translation_name_default));
        return new Language(code, name);
    }

    @Override
    public void setLanguages(Language source, Language translation) {
        updateSourceLanguage(source);
        updateTranslationLanguage(translation);
    }

    @Override
    public void setSourceLanguage(Language language) {
        Language translation = getTranslationLanguage();
        if (translation.equals(language)) {
            updateTranslationLanguage(getSourceLanguage());
        }
        updateSourceLanguage(language);
    }

    @Override
    public void setTranslationLanguage(Language language) {
        Language source = getSourceLanguage();
        if (source.equals(language)) {
            updateSourceLanguage(getTranslationLanguage());
        }
        updateTranslationLanguage(language);
    }

    private void updateSourceLanguage(Language language) {
        mPreferences.edit()
                .putString(SOURCE_CODE, language.getCode())
                .putString(SOURCE_NAME, language.getName())
                .apply();
    }

    private void updateTranslationLanguage(Language language) {
        mPreferences.edit()
                .putString(TRANSLATION_CODE, language.getCode())
                .putString(TRANSLATION_NAME, language.getName())
                .apply();
    }
}
