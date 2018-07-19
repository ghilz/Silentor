package com.example.alessandroghilardi.silentor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pinlist.db";
    // Books table name
    private static final String TABLE_NAME = "SilentPin";
    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_NAME = "name";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create books table
        db.execSQL("create table "
                + TABLE_NAME + " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_LATITUDE + " FLOAT not null, "
                + KEY_LONGITUDE+ " FLOAT not null,"
                + KEY_NAME+ " TEXT "
                + ");");
        Log.d(TAG, "onCreate(): create table");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // create fresh books table
        this.onCreate(db);
        Log.d(TAG, "onUpgrade(): created fresh table");
    }

    public long insertItem(PinItem item){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, item.getLatitude());
        values.put(KEY_LONGITUDE, item.getLongitude());
        values.put(KEY_NAME, item.getName());

        long id = db.insert(TABLE_NAME,
                null,
                values);

        Log.d(TAG, "insertItem("+id+") " + item.toString());

        db.close();
        return id;
    }

    public List<PinItem> getAllItems() {
        List<PinItem> items = new LinkedList<PinItem>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build book and add it to list
        PinItem dbPin = null;
        if (cursor.moveToFirst()) {
            do {
                dbPin = new PinItem();
                dbPin.setId(Integer.parseInt(cursor.getString(0)));

                double latitude = Double.parseDouble(cursor.getString(1));
                double longitude = Double.parseDouble(cursor.getString(2));

                LatLng coordinates = new LatLng(latitude, longitude);

                dbPin.setCoordinates(coordinates);
                dbPin.setName(cursor.getString(3));

                items.add(dbPin);      // Add item to items
            } while (cursor.moveToNext());
        }
        // 4. close
        db.close();
        Log.d(TAG, "getAllItems(): "+ items.toString());
        return items; // return items
    }

    public void deleteItem(PinItem item) {

        // 1. get reference to writable DB
        //    Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NAME, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(item.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d(TAG, "deleted item "+item.toString());

    }
}
