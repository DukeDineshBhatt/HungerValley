package com.dinesh.hungervalley;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static String DATABASE_NAME = "mynewdatabase";
    private static String TABLE_NAME = "mynewtable";
    private static String ID = "id";
    private static String NAME = "name";
    private static String COMPANY = "company";
    private static String EMAIL = "email";
    private static String MOBILE = "mobile";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String myquery = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER AUTO INCREMENT ," + NAME + " VARCHAR ," + COMPANY + " VARCHAR , " + EMAIL + " VARCHAR , " + MOBILE + " INTEGER)";
        db.execSQL(myquery);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }

    public boolean insertdata(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {

            return true;
        }


    }


}