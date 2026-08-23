package com.example.medicinereminderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MedicineDB";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // MEDICINE TABLE
        db.execSQL("CREATE TABLE medicines(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "dosage TEXT," +
                "time TEXT," +
                "startDate TEXT," +
                "endDate TEXT)");

        // HISTORY TABLE
        db.execSQL("CREATE TABLE history(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "status TEXT," +
                "date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS medicines");
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);
    }

    // ===============================
    // MEDICINE FUNCTIONS
    // ===============================

    // INSERT MEDICINE
    public void insertData(String name, String dose, String time, String start, String end) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("dosage", dose);
        cv.put("time", time);
        cv.put("startDate", start);
        cv.put("endDate", end);

        db.insert("medicines", null, cv);
        db.close();
    }

    // GET ALL MEDICINES
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM medicines ORDER BY id DESC", null);
    }

    // DELETE MEDICINE
    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("medicines", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ===============================
    // HISTORY FUNCTIONS
    // ===============================

    // INSERT HISTORY (Taken / Missed)
    public void insertHistory(String name, String status, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("status", status);
        cv.put("date", date);

        db.insert("history", null, cv);
        db.close();
    }

    // GET HISTORY (Latest first)
    public Cursor getHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM history ORDER BY id DESC", null);
    }

    // OPTIONAL: DELETE ALL HISTORY
    public void deleteAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM history");
        db.close();
    }

    // ADD THIS METHOD AT END OF DBHelper

    public int getTodayDoneCount(String todayDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM history WHERE status='Taken' AND date=?",
                new String[]{todayDate}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }
}