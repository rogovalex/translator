package ru.rogovalex.translator.data.translate;

import android.provider.BaseColumns;

public class TranslationTable implements BaseColumns {

    public static final String TABLE_NAME = "translation";

    public static final String TEXT = "text";
    public static final String TEXT_LANG = "text_lang";
    public static final String TRANSLATION = "translation";
    public static final String TRANSLATION_LANG = "translation_lang";
    public static final String FAVORITE = "favorite";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String CREATE_TABLE
            = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + TEXT + " TEXT,"
            + TEXT_LANG + " TEXT,"
            + TRANSLATION + " TEXT,"
            + TRANSLATION_LANG + " TEXT,"
            + FAVORITE + " INTEGER,"
            + "CONSTRAINT unq UNIQUE (" + TEXT + "," + TEXT_LANG + "," + TRANSLATION_LANG + ")"
            + ");";
}
