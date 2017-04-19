package ru.rogovalex.translator.data.database;

import android.provider.BaseColumns;

public class DefinitionTable implements BaseColumns {

    public static final String TABLE_NAME = "definition";

    public static final String TEXT = "text";
    public static final String TRANSCRIPTION = "transcription";
    public static final String POS = "pos";
    public static final String TRANSLATION_ID = "translation_id";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String CREATE_TABLE
            = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + TEXT + " TEXT,"
            + TRANSCRIPTION + " TEXT,"
            + POS + " TEXT,"
            + TRANSLATION_ID + " INTEGER,"
            + "FOREIGN KEY(" + TRANSLATION_ID + ") REFERENCES "
            + TranslationTable.TABLE_NAME + "(" + TranslationTable._ID + ")"
            + " ON DELETE CASCADE ON UPDATE CASCADE"
            + ");";
}
