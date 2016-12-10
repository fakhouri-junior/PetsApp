package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by salim on 20/10/2016.
 */
public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getName();

    /**
     * Constants for database name and version
     * used for the creation of the database
     * Note: increment the database version, when changes to the schema occurred
     */
    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;


    /**
     * constant string for the create table sql command
     */

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY_AUTO = " PRIMARY KEY AUTOINCREMENT";
    private static final String COMMA_SEP = ",";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT = " DEFAULT";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE "+ PetEntry.TABLE_NAME
            + "("+
            PetEntry._ID + INTEGER_TYPE + PRIMARY_KEY_AUTO + COMMA_SEP +
            PetEntry.COLUMN_PET_NAME + TEXT_TYPE + NOT_NULL +COMMA_SEP +
            PetEntry.COLUMN_PET_BREED + TEXT_TYPE + COMMA_SEP +
            PetEntry.COLUMN_PET_GENDER + INTEGER_TYPE + NOT_NULL +COMMA_SEP +
            PetEntry.COLUMN_PET_WEIGHT + INTEGER_TYPE + DEFAULT + " 0"
            +")";

    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + DATABASE_NAME;


    public PetDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the database
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.

    }
}
