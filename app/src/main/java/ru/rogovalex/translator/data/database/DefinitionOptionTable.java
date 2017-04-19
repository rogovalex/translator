package ru.rogovalex.translator.data.database;

import android.provider.BaseColumns;

public class DefinitionOptionTable implements BaseColumns {

    public static final String TABLE_NAME = "definition_option";

    public static final String SYNONYMS = "synonyms";
    public static final String MEANINGS = "meanings";
    public static final String EXAMPLES = "examples";
    public static final String DEFINITION_ID = "definition_id";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String CREATE_TABLE
            = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + SYNONYMS + " TEXT,"
            + MEANINGS + " TEXT,"
            + EXAMPLES + " TEXT,"
            + DEFINITION_ID + " INTEGER,"
            + "FOREIGN KEY(" + DEFINITION_ID + ") REFERENCES "
            + DefinitionTable.TABLE_NAME + "(" + DefinitionTable._ID + ")"
            + " ON DELETE CASCADE ON UPDATE CASCADE"
            + ");";
}
