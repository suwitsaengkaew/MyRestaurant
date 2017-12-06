package com.example.suwitsaengkaew.myrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by suwitsaengkaew on 20/11/2017 AD.
 */

public class FoodTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String FOOD_TABLE = "foodTABLE";
    public static final String COLUMN_ID_FOOD = "_id";
    public static final String COLUMN_FOOD = "Food";
    public static final String COLUMN_PRICE = "Price";

    public FoodTABLE(Context context) {

            objMyOpenHelper = new MyOpenHelper(context);
            writeSQLite = objMyOpenHelper.getWritableDatabase();
            readSQLite = objMyOpenHelper.getReadableDatabase();


    } // Constructor

    public String[] listPrice() {

        String strListPrice[] = null;
        Cursor objCursor = readSQLite.query(FOOD_TABLE, new String[] {COLUMN_ID_FOOD, COLUMN_PRICE}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListPrice = new String[objCursor.getCount()];

        for (int i = 0; i < objCursor.getCount(); i++) {
            strListPrice[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PRICE));
            objCursor.moveToNext();
        }
        objCursor.close();

        return strListPrice;
    } // listPrice


    public String[] listFood() {

        String strListFood[] = null;
        Cursor objCursor = readSQLite.query(FOOD_TABLE, new String[] {COLUMN_ID_FOOD, COLUMN_FOOD}, null, null, null, null, null);
        objCursor.moveToFirst();
        strListFood = new String[objCursor.getCount()];

        for (int i = 0; i < objCursor.getCount(); i++) {
            strListFood[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_FOOD));
            objCursor.moveToNext();
            Log.d("Restaurant", strListFood[i].toString());
        }
        objCursor.close();

        return strListFood;
    } // listFood

    // Add Value to Food Table
    public long addValueToFood(String strFood, String strPrice) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_PRICE, strPrice);

        return writeSQLite.insert(FOOD_TABLE, null, objContentValues);

    }

} // Main Class
