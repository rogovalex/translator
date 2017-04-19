package ru.rogovalex.translator;

import android.content.Context;
import android.content.SharedPreferences;

import ru.rogovalex.translator.domain.model.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 16:19
 */
public class PreferencesHelper {

    private static final String PREFERENCES = "ru.rogovalex.translator.preference";

    private static final String SOURCE_NAME = "sourceLangName";
    private static final String SOURCE_CODE = "sourceLangCode";
    private static final String TRANSLATION_NAME = "translationLangName";
    private static final String TRANSLATION_CODE = "translationLangCode";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public static Language getSourceLanguage(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return getSourceLanguage(context, preferences);
    }

    public static Language getTranslationLanguage(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return getTranslationLanguage(context, preferences);
    }

    public static void setSourceLanguage(Context context, Language language) {
        SharedPreferences preferences = getPreferences(context);
        Language translation = getTranslationLanguage(context, preferences);
        if (translation.getCode().equals(language.getCode())) {
            updateTranslationLanguage(preferences, getSourceLanguage(context, preferences));
        }
        updateSourceLanguage(preferences, language);
    }

    public static void setTranslationLanguage(Context context, Language language) {
        SharedPreferences preferences = getPreferences(context);
        Language source = getSourceLanguage(context, preferences);
        if (source.getCode().equals(language.getCode())) {
            updateSourceLanguage(preferences, getTranslationLanguage(context, preferences));
        }
        updateTranslationLanguage(preferences, language);
    }

    public static void setLanguages(Context context, Language source, Language translation) {
        SharedPreferences preferences = getPreferences(context);
        updateSourceLanguage(preferences, source);
        updateTranslationLanguage(preferences, translation);
    }

    private static Language getSourceLanguage(Context context, SharedPreferences preferences) {
        String code = preferences.getString(SOURCE_CODE,
                context.getString(R.string.source_code_default));
        String name = preferences.getString(SOURCE_NAME,
                context.getString(R.string.source_name_default));
        return new Language(code, name);
    }

    private static Language getTranslationLanguage(Context context, SharedPreferences preferences) {
        String code = preferences.getString(TRANSLATION_CODE,
                context.getString(R.string.translation_code_default));
        String name = preferences.getString(TRANSLATION_NAME,
                context.getString(R.string.translation_name_default));
        return new Language(code, name);
    }

    private static void updateSourceLanguage(SharedPreferences preferences, Language language) {
        preferences.edit()
                .putString(SOURCE_CODE, language.getCode())
                .putString(SOURCE_NAME, language.getName())
                .apply();
    }

    private static void updateTranslationLanguage(SharedPreferences preferences, Language language) {
        preferences.edit()
                .putString(TRANSLATION_CODE, language.getCode())
                .putString(TRANSLATION_NAME, language.getName())
                .apply();
    }
}
