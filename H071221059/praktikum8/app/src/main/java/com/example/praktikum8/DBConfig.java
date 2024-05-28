package com.example.praktikum8;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBConfig extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database-8";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "pertemuan_8";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_JUDUL = "judul";
    private static final String COLUMN_DESKRIPSI = "deskripsi";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    public DBConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_JUDUL + " TEXT,"
                + COLUMN_DESKRIPSI + " TEXT,"
                + COLUMN_CREATED_AT + " TEXT,"
                + COLUMN_UPDATED_AT + " TEXT)");
    }

    public void insertData(String judul, String deskripsi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUDUL, judul);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        String currentTime = getCurrentDateTime();
        values.put(COLUMN_CREATED_AT, currentTime);
        values.put(COLUMN_UPDATED_AT, currentTime);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllRecords() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor searchByTitle(String judul) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_JUDUL + " LIKE ?", new String[]{"%" + judul + "%"});
    }

    public void updateRecord(int id, String judul, String deskripsi) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUDUL, judul);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_UPDATED_AT, getCurrentDateTime());
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteRecords(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private String getCurrentDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getColumnId() {
        return COLUMN_ID;
    }

    public String getColumnJudul() {
        return COLUMN_JUDUL;
    }

    public String getColumnDeskripsi() {
        return COLUMN_DESKRIPSI;
    }

    public String getColumnCreatedAt() {
        return COLUMN_CREATED_AT;
    }

    public String getColumnUpdatedAt() {
        return COLUMN_UPDATED_AT;
    }
}