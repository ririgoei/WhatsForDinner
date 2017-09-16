package edu.sjsu.rmarcelita.whatsfordinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by riri on 9/12/17.
 */

public class SQLiteIngredientsHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ingredients";

    public SQLiteIngredientsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createIngredientsTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + createIngredientsTable());
        onCreate(sqLiteDatabase);
    }

    public String createIngredientsTable() {
        final String TABLE_NAME = "recipes";
        final String RECIPE_NAME = "recipeName";
        final String INGREDIENTS = "ingredients";
        final String INSTRUCTIONS = "instructions";
        final String IMAGE = "imagePath";

        final String CREATE_INGREDIENTS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                RECIPE_NAME + " TEXT unique, " +
                INGREDIENTS + " TEXT, " +
                INSTRUCTIONS + " TEXT, " +
                IMAGE + " TEXT " + ");";

        return CREATE_INGREDIENTS_TABLE;
    }

    public void createIngredientsOnlyTable() {
        final String TABLE_NAME = "ingredients";
        final String INGREDIENTS_NAME = "ingredientsName";

        final String CREATE_INGREDIENTS_ONLY_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                INGREDIENTS_NAME + " TEXT unique );";
        SQLiteDatabase ingDb = this.getWritableDatabase();

        ingDb.execSQL(CREATE_INGREDIENTS_ONLY_TABLE);
        ingDb.close();
    }

    public void insertIngredientsOnly(String ingredient) {
        SQLiteDatabase insertIngDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ingredientsName", ingredient);
        insertIngDb.insert("ingredients", null, contentValues);
        insertIngDb.close();
    }

    public ArrayList<String> getAllIngredients() {
        ArrayList<String> ingredientsName = new ArrayList<>();
        SQLiteDatabase allIngDb = this.getReadableDatabase();
        Cursor res = allIngDb.rawQuery("select ingredientsName from ingredients", null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            ingredientsName.add(res.getString(res.getColumnIndex("ingredientsName")));
            res.moveToNext();
        }
        allIngDb.close();
        return ingredientsName;
    }

    public void insertIntoTable(String recipeName, String ingredients, String instructions, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("recipeName", recipeName);
        contentValues.put("ingredients", ingredients);
        contentValues.put("instructions", instructions);
        contentValues.put("imagePath", imagePath);
        db.insert("recipes", null, contentValues);
        db.close();
    }

    public ArrayList<String> getAllRecipeName() {
        ArrayList<String> recipeArray = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select recipeName from recipes", null);
        res.moveToFirst();

        while(res.isAfterLast() == false) {
            recipeArray.add(res.getString(res.getColumnIndex("recipeName")));
            res.moveToNext();
        }
        db.close();
        return recipeArray;
    }
}
