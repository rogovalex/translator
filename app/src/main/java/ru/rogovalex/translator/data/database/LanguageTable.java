package ru.rogovalex.translator.data.database;

import android.provider.BaseColumns;

public class LanguageTable implements BaseColumns {

    public static final String TABLE_NAME = "language";

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String UI = "ui";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SELECT_BY_UI = UI + "=?";

    public static final String CREATE_TABLE
            = "CREATE TABLE " + TABLE_NAME + " ("
            + CODE + " TEXT,"
            + NAME + " TEXT,"
            + UI + " TEXT,"
            + "PRIMARY KEY (" + CODE + "," + UI + ")"
            + ");";
}
