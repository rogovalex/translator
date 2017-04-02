package ru.rogovalex.translator.data.translate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.domain.translate.Definition;
import ru.rogovalex.translator.domain.translate.Storage;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 12:46
 */
public class Database implements Storage {

    private final SQLiteOpenHelper mOpenHelper;

    @Inject
    public Database(SQLiteOpenHelper openHelper) {
        mOpenHelper = openHelper;
    }

    @Override
    public List<TranslateResult> getRecentTranslations() {
        return getTranslations(null);
    }

    private List<TranslateResult> getTranslations(String where) {
        List<TranslateResult> list = new ArrayList<>();

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(TranslationTable.TABLE_NAME, null,
                where, null, null, null, TranslationTable._ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            int colId = cursor.getColumnIndex(TranslationTable._ID);
            int colText = cursor.getColumnIndex(TranslationTable.TEXT);
            int colTextLang = cursor.getColumnIndex(TranslationTable.TEXT_LANG);
            int colTranslation = cursor.getColumnIndex(TranslationTable.TRANSLATION);
            int colTranslationLang = cursor.getColumnIndex(TranslationTable.TRANSLATION_LANG);
            int colFavorite = cursor.getColumnIndex(TranslationTable.FAVORITE);

            do {
                TranslateResult item = new TranslateResult(
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
                        getVariants(db, cursor.getInt(colId))));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    private List<Translation> getVariants(SQLiteDatabase db, int definitionId) {
        List<Translation> list = new ArrayList<>();

        Cursor cursor = db.query(VariantTable.TABLE_NAME, null,
                VariantTable.DEFINITION_ID + "=?",
                new String[]{String.valueOf(definitionId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int colSynonyms = cursor.getColumnIndex(VariantTable.SYNONYMS);
            int colMeanings = cursor.getColumnIndex(VariantTable.MEANINGS);
            int colExamples = cursor.getColumnIndex(VariantTable.EXAMPLES);

            do {
                list.add(new Translation(
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


    @Override
    public List<TranslateResult> getFavoriteTranslations() {
        return getTranslations(TranslationTable.FAVORITE + "=1");
    }

    @Override
    public void saveRecentTranslation(TranslateResult translation) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            SQLiteStatement insertTranslation = db.compileStatement(
                    "INSERT OR REPLACE INTO " + TranslationTable.TABLE_NAME + " ("
                            + TranslationTable.TEXT + ","
                            + TranslationTable.TEXT_LANG + ","
                            + TranslationTable.TRANSLATION + ","
                            + TranslationTable.TRANSLATION_LANG + ","
                            + TranslationTable.FAVORITE
                            + ") VALUES (?, ?, ?, ?, ?)");

            SQLiteStatement insertDefinition = db.compileStatement(
                    "INSERT INTO " + DefinitionTable.TABLE_NAME + " ("
                            + DefinitionTable.TRANSLATION_ID + ","
                            + DefinitionTable.TEXT + ","
                            + DefinitionTable.TRANSCRIPTION + ","
                            + DefinitionTable.POS
                            + ") VALUES (?, ?, ?, ?)");

            SQLiteStatement insertVariant = db.compileStatement(
                    "INSERT INTO " + VariantTable.TABLE_NAME + " ("
                            + VariantTable.DEFINITION_ID + ","
                            + VariantTable.SYNONYMS + ","
                            + VariantTable.MEANINGS + ","
                            + VariantTable.EXAMPLES
                            + ") VALUES (?, ?, ?, ?)");

            insertTranslation.bindString(1, translation.getText());
            insertTranslation.bindString(2, translation.getTextLang());
            insertTranslation.bindString(3, translation.getTranslation());
            insertTranslation.bindString(4, translation.getTranslationLang());
            insertTranslation.bindLong(5, translation.isFavorite() ? 1 : 0);

            long id = insertTranslation.executeInsert();

            for (Definition def : translation.getDefinitions()) {
                insertDefinition.clearBindings();
                insertDefinition.bindLong(1, id);
                insertDefinition.bindString(2, def.getText());
                insertDefinition.bindString(3, def.getTranscription());
                insertDefinition.bindString(4, def.getPos());

                long defId = insertDefinition.executeInsert();

                for (Translation item : def.getTranslations()) {
                    insertVariant.clearBindings();
                    insertVariant.bindLong(1, defId);
                    insertVariant.bindString(2, item.getSynonyms());
                    insertVariant.bindString(3, item.getMeanings());
                    insertVariant.bindString(4, item.getExamples());

                    insertVariant.executeInsert();
                }
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void saveFavoriteTranslation(TranslateResult translation) {

    }
}
