package com.example.nadya_praktikum2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper (Context context) {
        super(context, "restaurant.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE restaurant(id INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT, alamat TEXT, jenis TEXT, fasilitas TEXT)");
        db.execSQL("CREATE TABLE review(id INTEGER PRIMARY KEY AUTOINCREMENT, restaurantId INTEGER, nama TEXT, review TEXT, rating FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS restaurant");
        db.execSQL("DROP TABLE IF EXISTS review");
    }

    public boolean insertRestaurant (Restaurant rst) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", rst.getNama());
        cv.put("alamat", rst.getAlamat());
        cv.put("jenis", rst.getJenis());
        cv.put("fasilitas", rst.getFasilitas());

        return db.insert("restaurant", null, cv) > 0;
    }

    public boolean insertReview (Review rvw) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("restaurantId", rvw.getRestaurantId());
        cv.put("nama", rvw.getNama());
        cv.put("review", rvw.getReview());
        cv.put("rating", rvw.getRating());

        return db.insert("review", null, cv) > 0;
    }

    public Cursor getAllRestaurant () {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "restaurant", null);
    }

    public Cursor getRestaurant (int id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "restaurant" + " where id ="+id, null);
    }

    public Cursor getReviewRestaurant (int restaurantId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("Select * from " + "review"  + " where restaurantId =" + restaurantId, null);
    }

    public boolean updateRestaurant(Restaurant rst, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nama", rst.getNama());
        cv.put("alamat", rst.getAlamat());
        cv.put("jenis", rst.getJenis());
        cv.put("fasilitas", rst.getFasilitas());
        return db.update("restaurant", cv, "id" + "=" + id,
                null) > 0;
    }

    public boolean updateReview(Review rvw, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("restaurantId", rvw.getRestaurantId());
        cv.put("nama", rvw.getNama());
        cv.put("review", rvw.getReview());
        cv.put("rating", rvw.getRating());
        return db.update("review", cv, "id" + "=" + id,
                null) > 0;
    }

    public void deleteRestaurant(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("restaurant", "id" + "=" + id, null);
    }

    public void deleteReview(int id) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("review", "id" + "=" + id, null);
    }

    public Cursor getServiceCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT id AS _id, * FROM " + "restaurant", null);
    }
}
