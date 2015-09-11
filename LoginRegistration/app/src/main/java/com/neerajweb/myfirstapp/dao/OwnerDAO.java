package com.neerajweb.myfirstapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.neerajweb.myfirstapp.model.owner_model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 07/08/2015.
 */
public class OwnerDAO {

    public static final String TAG = "OwnerDAO";

    private Context mContext;
    public String sqldb_message;

    // Database fields
    private SQLiteDatabase mDatabase;
    private my_SqliteDatabaseHelper mDbHelper;

    public static final String OWNER_ID_WITH_PREFIX = "own.ID";
    public static final String OWNER_NAME_WITH_PREFIX = "own.NAME";
    public static final String FLAT_NAME_WITH_PREFIX = "flt.Flat_name";
    public static final String OWNER_USERNAME_WITH_PREFIX = "own.USERNAME";
    public static final String OWNER_PASSWORD_WITH_PREFIX = "own.PASSWORD";
    public static final String OWNER_EMAIL_WITH_PREFIX = "own.EMAIL";
    public static final String OWNER_AGE_WITH_PREFIX = "own.AGE";
    public static final String OWNER_STATUS_WITH_PREFIX = "own.APPROVESTATUS";


    private String[] mAllColumns = {  my_SqliteDatabaseHelper.Col_id,
            my_SqliteDatabaseHelper.Col_name,
            my_SqliteDatabaseHelper.Col_flat_id,
            my_SqliteDatabaseHelper.Col_username,
            my_SqliteDatabaseHelper.Col_password,
            my_SqliteDatabaseHelper.Col_emai,
            my_SqliteDatabaseHelper.Col_age,
            my_SqliteDatabaseHelper.Col_ApprovalStatus };

    public OwnerDAO(Context context) {
        mDbHelper = new my_SqliteDatabaseHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
            sqldb_message="System is ready";
        }
        catch(SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    /*
    * Insert an owner’s information
    */
    public owner_model createOwner(String name,Long flatno,String username,String password,String email,int age,int intStatus) {
        ContentValues values = new ContentValues();
        values.put(my_SqliteDatabaseHelper.Col_name, name);
        values.put(my_SqliteDatabaseHelper.Col_flat_id, flatno);
        values.put(my_SqliteDatabaseHelper.Col_username, username);
        values.put(my_SqliteDatabaseHelper.Col_password, password);
        values.put(my_SqliteDatabaseHelper.Col_emai, email);
        values.put(my_SqliteDatabaseHelper.Col_age, age);
        values.put(my_SqliteDatabaseHelper.Col_ApprovalStatus, intStatus);

        long insertId = mDatabase.insert(my_SqliteDatabaseHelper.TABLE_OWNER, null, values);
        Cursor cursor = mDatabase.query(my_SqliteDatabaseHelper.TABLE_OWNER,
                mAllColumns, my_SqliteDatabaseHelper.Col_id + " = " + insertId, null, null, null, null);

        cursor.moveToFirst();
        owner_model newOwner = cursorToOwner(cursor);
        cursor.close();
        return newOwner;
    }

    /*
    * Update an owner’s information
    */
    public int updateOwner(owner_model e) {
        ContentValues values = new ContentValues();
        values.put(my_SqliteDatabaseHelper.Col_name, e.getName());
        values.put(my_SqliteDatabaseHelper.Col_flat_id, e.getFlatno());
        values.put(my_SqliteDatabaseHelper.Col_username, e.getUsername());
        values.put(my_SqliteDatabaseHelper.Col_password, e.getPassword());
        values.put(my_SqliteDatabaseHelper.Col_emai, e.getEmail());
        values.put(my_SqliteDatabaseHelper.Col_age, e.getAge());
        values.put(my_SqliteDatabaseHelper.Col_ApprovalStatus, e.getStatus());

        // updating row
        return mDatabase.update(my_SqliteDatabaseHelper.TABLE_OWNER, values, my_SqliteDatabaseHelper.Col_id + " = ?",
        new String[] { String.valueOf(e.getId()) });
    }

    /*
    * Update an Approval Status in owner’s information
    */
    public int updateApprovalStatus(owner_model e) {
        ContentValues values = new ContentValues();
        values.put(my_SqliteDatabaseHelper.Col_ApprovalStatus, e.getStatus());

        // updating Approval Status
        return mDatabase.update(my_SqliteDatabaseHelper.TABLE_OWNER, values, my_SqliteDatabaseHelper.Col_id + " = ?",
                new String[] { String.valueOf(e.getId()) });
    }

    public void deleteOwner(owner_model Owner) {
        long id = Owner.getId();
        System.out.println("the deleted Owner has the id: " + id);
        mDatabase.delete(my_SqliteDatabaseHelper.TABLE_OWNER, my_SqliteDatabaseHelper.Col_id + " = " + id, null);
    }

    public List<owner_model> getAllOwners() {
        List<owner_model> listOwners = new ArrayList<owner_model>();

        Cursor cursor = mDatabase.query(my_SqliteDatabaseHelper.TABLE_OWNER,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            owner_model Owner = cursorToOwner(cursor);
            listOwners.add(Owner);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listOwners;
    }

    public List<owner_model> getOwnerbyId(long ownerId) {
        List<owner_model> listOwners = new ArrayList<owner_model>();

        Cursor cursor = mDatabase.query(my_SqliteDatabaseHelper.TABLE_OWNER, mAllColumns
                , my_SqliteDatabaseHelper.Col_id + " = ?",
                new String[]{String.valueOf(ownerId)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            owner_model Owner = cursorToOwner(cursor);
            listOwners.add(Owner);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listOwners;
    }

    public List<owner_model> getOwnerbyUnamePwd(String sUsername, String sPwd) {
        List<owner_model> listOwners = new ArrayList<owner_model>();

        //Identity column should be named "_id" in Android SQLite
        String sqldb_query = "SELECT " + OWNER_ID_WITH_PREFIX +" as _id , "+ OWNER_NAME_WITH_PREFIX +" , " + FLAT_NAME_WITH_PREFIX + " , "
                + OWNER_USERNAME_WITH_PREFIX +" , "+ OWNER_PASSWORD_WITH_PREFIX +" , "+ OWNER_EMAIL_WITH_PREFIX +" , " + OWNER_AGE_WITH_PREFIX + " , " + OWNER_STATUS_WITH_PREFIX + " FROM "
                + my_SqliteDatabaseHelper.TABLE_OWNER + " own, "
                + my_SqliteDatabaseHelper.TABLE_FLATS + " flt WHERE own."
                + my_SqliteDatabaseHelper.Col_flat_id + " = flt."
                + my_SqliteDatabaseHelper.Col_FLAT_ID
                + " AND own." + my_SqliteDatabaseHelper.Col_username + "= '" + sUsername + "' AND own." + my_SqliteDatabaseHelper.Col_password +"= '" + sPwd + "';";

        Cursor cursor = mDatabase.rawQuery(sqldb_query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            owner_model Owner = cursorToOwner(cursor);
            listOwners.add(Owner);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listOwners;
    }

    public Cursor fetchOwnerbyUnamePwd(String sUsername, String sPwd) throws SQLException {
            //Identity column should be named "_id" in Android SQLite
            String sqldb_query = "SELECT " + OWNER_ID_WITH_PREFIX +" as _id , "+ OWNER_NAME_WITH_PREFIX +" , " + FLAT_NAME_WITH_PREFIX + " , "
                    + OWNER_AGE_WITH_PREFIX + " FROM "
                    + my_SqliteDatabaseHelper.TABLE_OWNER + " own, "
                    + my_SqliteDatabaseHelper.TABLE_FLATS + " flt WHERE own."
                    + my_SqliteDatabaseHelper.Col_flat_id + " = flt."
                    + my_SqliteDatabaseHelper.Col_FLAT_ID
                    + " AND own." + my_SqliteDatabaseHelper.Col_username + "= '" + sUsername + "' AND own." + my_SqliteDatabaseHelper.Col_password +"= '" + sPwd + "';";

            Cursor mCursor = mDatabase.rawQuery(sqldb_query, null);

            if (mCursor != null) {
                sqldb_message="Welcome to appartment app";
                mCursor.moveToFirst();
            }
            else
            {
                sqldb_message="You are not an authorised user please register";
            }
        return mCursor;
    }

    private owner_model cursorToOwner(Cursor cursor) {
        owner_model Owner = new owner_model();
        Owner.setId(cursor.getLong(0));
        Owner.setName(cursor.getString(1));
        Owner.setFlatno(cursor.getString(2));
        Owner.setUsername(cursor.getString(3));
        Owner.setPassword(cursor.getString(4));
        Owner.setEmail(cursor.getString(5));
        Owner.setAge(cursor.getInt(6));
        Owner.setStatus(cursor.getInt(7));
        return Owner;
    }
}