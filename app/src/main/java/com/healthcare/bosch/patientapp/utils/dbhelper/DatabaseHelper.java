package com.healthcare.bosch.patientapp.utils.dbhelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "MEDICATION";

    // Table columns
    public static final String _ID = "_id";
    public static final String _PATIENTID = "patientId";
    public static final String SUBJECT = "subject";
    public static final String DESC = "description";
    public static final String STATUS = "status";
    public static final String DATE = "date";

    // Database Information
    static final String DB_NAME = "MEDICATIONREQUEST.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STATUS + " TEXT NOT NULL, " + SUBJECT + " TEXT NOT NULL, " + _PATIENTID + " TEXT NOT NULL, " + DESC + " TEXT , " + DATE + " TEXT  );";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}