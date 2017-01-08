package com.codenrun.deaddropper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salma on 2017-01-08.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MarkerManager";

    // Contacts table name
    private static final String TABLE_MARKERS = "markers";

    // Contacts Table Columns names
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MARKERS_TABLE = "CREATE TABLE " + TABLE_MARKERS + "("
                + KEY_LATITUDE + "REAL" + KEY_LONGITUDE + "REAL"
                + KEY_NAME + "TEXT" + KEY_DESCRIPTION + "TEXT" + ")";
        db.execSQL(CREATE_MARKERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKERS);

        // create tables again
        onCreate(db);
    }

    // add new marker
    public void addMarker(Marker marker){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, marker.get_latitude());
        values.put(KEY_LONGITUDE, marker.get_longitude());
        values.put(KEY_NAME, marker.get_name());
        values.put(KEY_DESCRIPTION, marker.get_description());

        // inserting row
        db.insert(TABLE_MARKERS, null, values);
        db.close(); // closing database connection
    }

    // getting single contact
    public Marker getMarker(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MARKERS, new String[] { KEY_LATITUDE, KEY_LONGITUDE,
                KEY_NAME, KEY_DESCRIPTION}, KEY_NAME + "=?",
                new String[] {String.valueOf(name)}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        Marker marker = new Marker(Double.parseDouble(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3));
        // return marker
        return marker;
    }

    // getting all contacts
    public List<Marker> getAllMarkers(){
        List<Marker> markerList = new ArrayList<Marker>();
        // select all query
        String selectQuery = "SELECT * FROM " + TABLE_MARKERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Marker marker = new Marker();
                marker.set_latitude(Double.parseDouble(cursor.getString(0)));
                marker.set_longitude(Double.parseDouble(cursor.getString(1)));
                marker.set_name(cursor.getString(2));
                marker.set_description(cursor.getString(3));
                // adding contact to list
                markerList.add(marker);
            } while (cursor.moveToNext());
        }

        // return marker list
        return markerList;
    }

    // get markers count
    public int getMarkersCount(){
        String countQuery = "SELECT * FROM " + TABLE_MARKERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // updating single marker
    public int updateMarker(Marker marker){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, marker.get_latitude());
        values.put(KEY_LONGITUDE, marker.get_longitude());
        values.put(KEY_NAME, marker.get_name());
        values.put(KEY_DESCRIPTION, marker.get_description());

        // updating row
        return db.update(TABLE_MARKERS, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(marker.get_name()) });
    }

    // deleting single marker
    public void deleteMarker(Marker marker) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MARKERS, KEY_NAME + " = ?",
                new String[] { String.valueOf(marker.get_name()) });
        db.close();
    }
}
