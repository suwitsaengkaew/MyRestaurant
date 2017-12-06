package com.example.suwitsaengkaew.myrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by suwitsaengkaew on 15/11/2017 AD.
 */

public class OrderTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;

    public static final String TABLE_ORDER = "orderTABLE";
    public static final String COLUMN_ID_ORDER = "_id";
    public static final String COLUMN_OFFICER = "Officer";
    public static final String COLUMN_DATE = "Desk";
    public static final String COLUMN_FOOD = "Food";
    public static final String COLUMN_ITEM = "Item";

    public OrderTABLE (Context context) {

            objMyOpenHelper = new MyOpenHelper(context);
            writeSQLite = objMyOpenHelper.getWritableDatabase();
            readSQLite = objMyOpenHelper.getReadableDatabase();
    } // Constructor

    public long addValueToOrder(String strOfficer, String strDate, String strFood, int intItem) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_OFFICER, strOfficer);
        objContentValues.put(COLUMN_DATE, strDate);
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_ITEM, intItem);

        return writeSQLite.insert(TABLE_ORDER, null, objContentValues);
    }
} // Main Class
