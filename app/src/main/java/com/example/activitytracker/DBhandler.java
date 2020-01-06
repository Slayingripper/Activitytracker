package com.example.activitytracker;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public void addLog(Sport sport) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATETIME, sport.getdatetime ());
        values.put(COLUMN_DISTANCE,sport.getdistance ());
        values.put(COLUMN_SPEED,sport.getspeed ());
        values.put(COLUMN_TIME, sport.gettime ());
        mycr.insert(MyContentProvider.CONTENT_URI,values);
    }
    public  Cursor getinfo(){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor data  = db.rawQuery ("SELECT * FROM " + TABLE_LOGS + " ORDER by time", null  );
        return data;
    }
    public  Cursor getfastest(){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor data  = db.rawQuery ("SELECT * FROM " + TABLE_LOGS + " ORDER by distance DESC", null  );
        return data;
    }
    public  Cursor getoldest(){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor data  = db.rawQuery ("SELECT * FROM " + TABLE_LOGS + " ORDER by datetime DESC", null  );
        return data;
    }
    public void removeSinglerow(String chicken) {
        //Open the database
        SQLiteDatabase db = this.getWritableDatabase();
        //Execute sql query to remove from database
        Log.d ( "GOT THE DISTANCE", String.valueOf ( chicken ) );
        db.delete ( TABLE_LOGS, "time = '" + chicken + "'", null );

        db.close();
    }
}