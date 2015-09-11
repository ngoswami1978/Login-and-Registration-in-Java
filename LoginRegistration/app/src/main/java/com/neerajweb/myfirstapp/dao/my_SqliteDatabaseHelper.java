package com.neerajweb.myfirstapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.lang.String ;


/**
 * Created by Admin on 07/08/2015.
 */
public class my_SqliteDatabaseHelper  extends SQLiteOpenHelper {
    public static final String TAG = "my_SqliteDatabaseHelper";

    public static final String  DATABASE_NAME ="Apartment.db";
    public static final String  TABLE_OWNER ="A_APT_OWNER_MASTER";
    public static final String TABLE_FLATS = "A_APT_FLAT";

    public static final  int DATABASEVERSION=6;

    // columns of the Owner table
    public static final String  Col_id ="ID";
    public static final String  Col_name ="NAME";
    public static final String  Col_flat_id ="FLATNO";
    public static final String  Col_username ="USERNAME";
    public static final String  Col_password ="PASSWORD";
    public static final String  Col_emai ="EMAIL";
    public static final String  Col_age ="AGE";
    public static final String  Col_ApprovalStatus ="APPROVESTATUS";

    // columns of the Appartment flat table
    public static final String Col_FLAT_ID = "_id";
    public static final String Col_FLAT_NAME = "Flat_name";
    public static final String Col_FLAT_TYPE = "Flat_type";

    // SQL statement of the employees table creation
    private static final String strSQL_CREATE_TABLE_OWNER = "CREATE TABLE " + TABLE_OWNER + "("
            + Col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Col_name + " VARCHAR(50) , "
            + Col_flat_id + " INTEGER, "
            + Col_username + " VARCHAR(25), "
            + Col_password + " VARCHAR(25), "
            + Col_emai + " VARCHAR(100), "
            + Col_age + " INTEGER ,"
            + Col_ApprovalStatus + " INTEGER "
            +");";

    // SQL statement of the companies table creation
    private static final String strSQL_CREATE_TABLE_FLATS = "CREATE TABLE " + TABLE_FLATS + "("
            + Col_FLAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Col_FLAT_NAME + " VARCHAR(10) NOT NULL, "
            + Col_FLAT_TYPE + " INTEGER " // 1,2,3 BHK
            +");";


    public my_SqliteDatabaseHelper(Context context )
    {
        super(context, DATABASE_NAME, null, DATABASEVERSION);
    }

    public my_SqliteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context, DATABASE_NAME, factory, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(strSQL_CREATE_TABLE_OWNER);
        database.execSQL(strSQL_CREATE_TABLE_FLATS );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            Log.w(TAG, "Upgrading the database from version " + oldVersion + " to " + newVersion);
            // clear all data
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWNER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLATS);
            // recreate the tables
            onCreate(db);
        }
    }
}
