package ru.rogovalex.translator;

import android.content.Context;
import android.content.SharedPreferences;

import ru.rogovalex.translator.domain.translate.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 16:19
 */
public class PreferencesHelper {

    private static final String PREFERENCES = "ru.rogovalex.translator.preference";

    private static final String ORIGIN_NAME = "originLangName";
    private static final String ORIGIN_CODE = "originLangCode";
    private static final String TRANSLATION_NAME = "translationLangName";
    private static final String TRANSLATION_CODE = "translationLangCode";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public static Language getOriginLanguage(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return getOriginLanguage(context, preferences);
    }

    public static Language getTranslationLanguage(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return getTranslationLanguage(context, preferences);
    }

    public static void setOriginLanguage(Context context, Language language) {
        SharedPreferences preferences = getPreferences(context);
        Language translation = getTranslationLanguage(context, preferences);
        if (translation.getCode().equals(language.getCode())) {
            updateTranslationLanguage(preferences, getOriginLanguage(context, preferences));
        }
        updateOriginLanguage(preferences, language);
    }

    public static void setTranslationLanguage(Context context, Language language) {
        SharedPreferences preferences = getPreferences(context);
        Language origin = getOriginLanguage(context, preferences);
        if (origin.getCode().equals(language.getCode())) {
            updateOriginLanguage(preferences, getTranslationLanguage(context, preferences));
        }
        updateTranslationLanguage(preferences, language);
    }

    public static void setLanguages(Context context, Language origin, Language translation) {
        SharedPreferences preferences = getPreferences(context);
        updateOriginLanguage(preferences, origin);
        updateTranslationLanguage(preferences, translation);
    }

    private static Language getOriginLanguage(Context context, SharedPreferences preferences) {
        String code = preferences.getString(ORIGIN_CODE,
                context.getString(R.string.origin_code_default));
        String name = preferences.getString(ORIGIN_NAME,
                context.getString(R.string.origin_name_default));
        return new Language(code, name);
    }

    private static Language getTranslationLanguage(Context context, SharedPreferences preferences) {
        String code = preferences.getString(TRANSLATION_CODE,
                context.getString(R.string.translation_code_default));
        String name = preferences.getString(TRANSLATION_NAME,
                context.getString(R.string.translation_name_default));
        return new Language(code, name);
    }

    private static void updateOriginLanguage(SharedPreferences preferences, Language language) {
        preferences.edit()
                .putString(ORIGIN_CODE, language.getCode())
                .putString(ORIGIN_NAME, language.getName())
                .apply();
    }

    private static void updateTranslationLanguage(SharedPreferences preferences, Language language) {
        preferences.edit()
                .putString(TRANSLATION_CODE, language.getCode())
                .putString(TRANSLATION_NAME, language.getName())
                .apply();
    }
}
