package com.neerajweb.myfirstapp.dao;

/**
 * Created by Admin on 10/08/2015.
 */

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.neerajweb.myfirstapp.model.flat_model;
import com.neerajweb.myfirstapp.model.owner_model;


public class FlatDAO {

    public static final String TAG = "FlatDAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private my_SqliteDatabaseHelper mDbHelper;

    private Context mContext;

    private String[] mAllColumns = {my_SqliteDatabaseHelper.Col_FLAT_ID,my_SqliteDatabaseHelper.Col_FLAT_NAME, my_SqliteDatabaseHelper.Col_FLAT_TYPE };

    public FlatDAO(Context context) {
        this.mContext = context;
        mDbHelper = new my_SqliteDatabaseHelper(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public flat_model createFlats(String name, String flat_type) {
        ContentValues values = new ContentValues();
        values.put(my_SqliteDatabaseHelper.Col_FLAT_NAME, name);
        values.put(my_SqliteDatabaseHelper.Col_FLAT_TYPE, flat_type);

        long insertId = mDatabase
                .insert(my_SqliteDatabaseHelper.TABLE_FLATS, null, values);

        Cursor cursor = mDatabase.query(my_SqliteDatabaseHelper.TABLE_FLATS, mAllColumns,
                my_SqliteDatabaseHelper.Col_FLAT_ID + " = " + insertId, null, null,
                null, null);

        cursor.moveToFirst();
        flat_model newCompany = cursorToFlat(cursor);
        cursor.close();
        return newCompany;
    }

    public List<flat_model> getAllFlats() {
        List<flat_model> listFlats = new ArrayList<flat_model>();

        Cursor cursor = mDatabase.query(my_SqliteDatabaseHelper.TABLE_FLATS, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                flat_model flat = cursorToFlat(cursor);
                listFlats.add(flat);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
        }
        return listFlats;
    }

    public flat_model getFlatById(long id) {
        Cursor cursor = mDatabase.query(my_SqliteDatabaseHelper.TABLE_FLATS, mAllColumns,
                my_SqliteDatabaseHelper.Col_FLAT_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        flat_model flats = cursorToFlat(cursor);
        return flats;
    }

    protected flat_model cursorToFlat(Cursor cursor) {
        flat_model flat = new flat_model();
        flat.setId(cursor.getInt (0));
        flat.setName(cursor.getString(1));
        flat.setFlatType(cursor.getString(2));
        return flat;
    }
}