package ru.rogovalex.translator.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import ru.rogovalex.translator.domain.translate.Definition;
import ru.rogovalex.translator.domain.translate.DefinitionOption;
import ru.rogovalex.translator.domain.translate.Language;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:46
 */
public class Database {

    private final SQLiteOpenHelper mOpenHelper;

    public Database(SQLiteOpenHelper openHelper) {
        mOpenHelper = openHelper;
    }

    public List<Translation> getRecentTranslations() {
        return getTranslations(TranslationTable.HISTORY + "=?", new String[]{"1"});
    }

    public List<Translation> getTranslations(TranslateParams params) {
        return getTranslations(TranslationTable.TEXT + "=? AND "
                        + TranslationTable.TEXT_LANG + "=? AND "
                        + TranslationTable.TRANSLATION_LANG + "=?",
                new String[]{params.getText(), params.getTextLang(),
                        params.getTranslationLang()});
    }

    private List<Translation> getTranslations(String where, String[] args) {
        List<Translation> list = new ArrayList<>();

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(TranslationTable.TABLE_NAME, null,
                where, args, null, null, TranslationTable.TIMESTAMP + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            int colId = cursor.getColumnIndex(TranslationTable._ID);
            int colText = cursor.getColumnIndex(TranslationTable.TEXT);
            int colTextLang = cursor.getColumnIndex(TranslationTable.TEXT_LANG);
            int colTranslation = cursor.getColumnIndex(TranslationTable.TRANSLATION);
            int colTranslationLang = cursor.getColumnIndex(TranslationTable.TRANSLATION_LANG);
            int colFavorite = cursor.getColumnIndex(TranslationTable.FAVORITE);

            do {
                Translation item = new Translation(
                        cursor.getString(colText),
                        cursor.getString(colTextLang),
                        cursor.getString(colTranslation),
                        cursor.getString(colTranslationLang),
                        getDefinitions(db, cursor.getInt(colId)));
                item.setFavorite(cursor.getInt(colFavorite) == 1);
                list.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    private List<Definition> getDefinitions(SQLiteDatabase db, int translationId) {
        List<Definition> list = new ArrayList<>();

        Cursor cursor = db.query(DefinitionTable.TABLE_NAME, null,
                DefinitionTable.TRANSLATION_ID + "=?",
                new String[]{String.valueOf(translationId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int colId = cursor.getColumnIndex(DefinitionTable._ID);
            int colText = cursor.getColumnIndex(DefinitionTable.TEXT);
            int colTranscription = cursor.getColumnIndex(DefinitionTable.TRANSCRIPTION);
            int colPos = cursor.getColumnIndex(DefinitionTable.POS);

            do {
                list.add(new Definition(
                        cursor.getString(colText),
                        cursor.getString(colTranscription),
                        cursor.getString(colPos),
                        getDefinitionOptions(db, cursor.getInt(colId))));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    private List<DefinitionOption> getDefinitionOptions(SQLiteDatabase db, int definitionId) {
        List<DefinitionOption> list = new ArrayList<>();

        Cursor cursor = db.query(DefinitionOptionTable.TABLE_NAME, null,
                DefinitionOptionTable.DEFINITION_ID + "=?",
                new String[]{String.valueOf(definitionId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int colSynonyms = cursor.getColumnIndex(DefinitionOptionTable.SYNONYMS);
            int colMeanings = cursor.getColumnIndex(DefinitionOptionTable.MEANINGS);
            int colExamples = cursor.getColumnIndex(DefinitionOptionTable.EXAMPLES);

            do {
                list.add(new DefinitionOption(
                        cursor.getString(colSynonyms),
                        cursor.getString(colMeanings),
                        cursor.getString(colExamples)));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public List<Translation> getFavoriteTranslations() {
        return getTranslations(TranslationTable.FAVORITE + "=?", new String[]{"1"});
    }

    public boolean saveRecentTranslation(Translation translation) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            SQLiteStatement insertTranslation = db.compileStatement(
                    "INSERT OR REPLACE INTO " + TranslationTable.TABLE_NAME + " ("
                            + TranslationTable._ID + ","
                            + TranslationTable.TEXT + ","
                            + TranslationTable.TEXT_LANG + ","
                            + TranslationTable.TRANSLATION + ","
                            + TranslationTable.TRANSLATION_LANG + ","
                            + TranslationTable.HISTORY + ","
                            + TranslationTable.FAVORITE + ","
                            + TranslationTable.TIMESTAMP
                            + ") VALUES ((SELECT " + TranslationTable._ID
                            + " FROM " + TranslationTable.TABLE_NAME + " WHERE "
                            + TranslationTable.TEXT + "=? AND "
                            + TranslationTable.TEXT_LANG + "=? AND "
                            + TranslationTable.TRANSLATION_LANG + "=?"
                            + "), ?, ?, ?, ?, 1, ?, ?)");

            SQLiteStatement insertDefinition = db.compileStatement(
                    "INSERT INTO " + DefinitionTable.TABLE_NAME + " ("
                            + DefinitionTable.TRANSLATION_ID + ","
                            + DefinitionTable.TEXT + ","
                            + DefinitionTable.TRANSCRIPTION + ","
                            + DefinitionTable.POS
                            + ") VALUES (?, ?, ?, ?)");

            SQLiteStatement insertVariant = db.compileStatement(
                    "INSERT INTO " + DefinitionOptionTable.TABLE_NAME + " ("
                            + DefinitionOptionTable.DEFINITION_ID + ","
                            + DefinitionOptionTable.SYNONYMS + ","
                            + DefinitionOptionTable.MEANINGS + ","
                            + DefinitionOptionTable.EXAMPLES
                            + ") VALUES (?, ?, ?, ?)");

            insertTranslation.bindString(1, translation.getText());
            insertTranslation.bindString(2, translation.getTextLang());
            insertTranslation.bindString(3, translation.getTranslationLang());
            insertTranslation.bindString(4, translation.getText());
            insertTranslation.bindString(5, translation.getTextLang());
            insertTranslation.bindString(6, translation.getTranslation());
            insertTranslation.bindString(7, translation.getTranslationLang());
            insertTranslation.bindLong(8, translation.isFavorite() ? 1 : 0);
            insertTranslation.bindLong(9, System.currentTimeMillis());

            long id = insertTranslation.executeInsert();

            for (Definition def : translation.getDefinitions()) {
                insertDefinition.clearBindings();
                insertDefinition.bindLong(1, id);
                insertDefinition.bindString(2, def.getText());
                insertDefinition.bindString(3, def.getTranscription());
                insertDefinition.bindString(4, def.getPos());

                long defId = insertDefinition.executeInsert();

                for (DefinitionOption item : def.getDefinitionOptions()) {
                    insertVariant.clearBindings();
                    insertVariant.bindLong(1, defId);
                    insertVariant.bindString(2, item.getSynonyms());
                    insertVariant.bindString(3, item.getMeanings());
                    insertVariant.bindString(4, item.getExamples());

                    insertVariant.executeInsert();
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean updateFavoriteTranslation(Translation translation) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        SQLiteStatement updateFavorite = db.compileStatement(
                "UPDATE " + TranslationTable.TABLE_NAME + " SET "
                        + TranslationTable.FAVORITE + "=? WHERE "
                        + TranslationTable.TEXT + "=? AND "
                        + TranslationTable.TEXT_LANG + "=? AND "
                        + TranslationTable.TRANSLATION_LANG + "=?");

        updateFavorite.bindLong(1, translation.isFavorite() ? 1 : 0);
        updateFavorite.bindString(2, translation.getText());
        updateFavorite.bindString(3, translation.getTextLang());
        updateFavorite.bindString(4, translation.getTranslationLang());

        int countRows = updateFavorite.executeUpdateDelete();
        return countRows > 0;
    }

    public List<Language> getLanguages(String uiLang) {
        List<Language> list = new ArrayList<>();

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(LanguageTable.TABLE_NAME, null,
                LanguageTable.UI + "=?", new String[]{uiLang},
                null, null, LanguageTable.NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            int colCode = cursor.getColumnIndex(LanguageTable.CODE);
            int colName = cursor.getColumnIndex(LanguageTable.NAME);

            do {
                Language item = new Language(
                        cursor.getString(colCode),
                        cursor.getString(colName));
                list.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public boolean saveLanguages(String uiLang, List<Language> languages) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            SQLiteStatement insertLang = db.compileStatement(
                    "INSERT OR REPLACE INTO " + LanguageTable.TABLE_NAME + " ("
                            + LanguageTable.CODE + ","
                            + LanguageTable.NAME + ","
                            + LanguageTable.UI
                            + ") VALUES (?, ?, ?)");

            for (Language lang : languages) {
                insertLang.clearBindings();
                insertLang.bindString(1, lang.getCode());
                insertLang.bindString(2, lang.getName());
                insertLang.bindString(3, uiLang);
                insertLang.executeInsert();
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
        return true;
    }
}
