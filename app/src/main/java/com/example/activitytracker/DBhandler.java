package com.example.activitytracker;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBhandler extends SQLiteOpenHelper {
    SQLiteDatabase.CursorFactory factory;
    private ContentResolver mycr;
    public static final String TABLE_LOGS ="logstable",
            COLUMN_DISTANCE="distance",
            COLUMN_DATETIME = "datetime",
            COLUMN_TIME ="time",
            COLUMN_SPEED = "speed";
    private static final int DATABASE_VERSION = 2;
    public static final String TAG = "Activitytrackerdb";
    private static final String DATABASE_NAME = "activitydb.db";
    public DBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mycr = context.getContentResolver();
    }
    //creates the database 
    // using text is much easier to handle since we can just use
    // "parse" later on if we need something more specific 
    // this makes us reduce the amount of errors  early 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGS_TABLE = "CREATE TABLE " +
                TABLE_LOGS + "("
                + COLUMN_DATETIME + " TEXT," +
                COLUMN_DISTANCE
                + " TEXT,"+ COLUMN_SPEED +" TEXT, " + COLUMN_TIME + " TEXT)";
        db.execSQL(CREATE_LOGS_TABLE);
    }
    @Override
    
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    } 
    //adds logs to the database 
    public void addLog(Sport sport) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATETIME, sport.getdatetime ());
        values.put(COLUMN_DISTANCE,sport.getdistance ());
        values.put(COLUMN_SPEED,sport.getspeed ());
        values.put(COLUMN_TIME, sport.gettime ());
        mycr.insert(MyContentProvider.CONTENT_URI,values);
    }
    //shows all the logs 
    public  Cursor getinfo(){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor data  = db.rawQuery ("SELECT * FROM " + TABLE_LOGS + " ORDER by time", null  );
        return data;
    }
    //shows logs which show running
    public  Cursor getfastest(){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor data  = db.rawQuery ("SELECT * FROM logstable WHERE speed >=8", null  );
        return data;
    }
    //shows disances over 10km log
    public  Cursor getfurthest(){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor data  = db.rawQuery ("SELECT * FROM logstable WHERE distance >= 10", null  );
        return data;
    }

    //gets the total amount of KM you ranA
    public  Cursor gettotal(){
        SQLiteDatabase db = this.getWritableDatabase ();

        Cursor data  = db.rawQuery ("SELECT COUNT(distance)" + "FROM logstable;", null  );
        int total = data.getInt(data.getColumnIndex("myTotal"));
       // tt.getInt("total");
        return data;
    }
    // this function removes a single row of logs from the database
    //by grabbing its possition using the the variable "chicken"
    public void removeSinglerow(String chicken) {
        //Open the database
        SQLiteDatabase db = this.getWritableDatabase();
        //Execute sql query to remove from database
        Log.d ( "GOT THE DISTANCE", String.valueOf ( chicken ) );
        db.delete ( TABLE_LOGS, "time = '" + chicken + "'", null );

        db.close();
    }
}
