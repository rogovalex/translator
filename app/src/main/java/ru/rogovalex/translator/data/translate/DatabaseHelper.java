package ru.rogovalex.translator.data.translate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:52
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "translations.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TranslationTable.CREATE_TABLE);
        db.execSQL(DefinitionTable.CREATE_TABLE);
        db.execSQL(VariantTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TranslationTable.DROP_TABLE);
        db.execSQL(DefinitionTable.DROP_TABLE);
        db.execSQL(VariantTable.DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
