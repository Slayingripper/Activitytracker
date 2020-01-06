package com.example.activitytracker;

import android.net.Uri;

public class Contract {
    private static final String AUTHORITY = "com.exmple.activitytracker.MyContentProvider";
    public static final Uri LOGS_URI = Uri.parse("content://"+AUTHORITY+"/TABLE_LOGS");

    public static final String DATETIME = "_datetime";
    public static final String DISTANCE = "_distance";
    public static final String TIME = "_time";
    public static final String SPEED = "speed";


}
