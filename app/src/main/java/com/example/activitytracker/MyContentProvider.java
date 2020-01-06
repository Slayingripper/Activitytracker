package com.example.activitytracker;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider{
    public static final int LOGS = 1;
    private static final String AUTHORITY = "com.example.activitytracker.MyContentProvider";
    private static final String LOGS_TABLE = "logstable";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LOGS_TABLE);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int logstable = 1;

    private DBhandler db;
    static {
        sURIMatcher.addURI(AUTHORITY, LOGS_TABLE, LOGS);
        //sURIMatcher.addURI(AUTHORITY, LOGS_TABLE + "/#",logstable);
    }

    public MyContentProvider(){
    }
    @Override
    public boolean onCreate() {
        db = new DBhandler(getContext() );
        return false;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBhandler.TABLE_LOGS);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case LOGS:
                queryBuilder.appendWhere(DBhandler.COLUMN_DATETIME + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(db.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }
    @Nullable
    @Override
    //function for inserting the data into the database
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = db.getWritableDatabase();
        long id = 0;

        switch (uriType){
            case LOGS:
                id = sqlDB.insert(db.TABLE_LOGS,null, values);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(LOGS_TABLE +"/" +id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
