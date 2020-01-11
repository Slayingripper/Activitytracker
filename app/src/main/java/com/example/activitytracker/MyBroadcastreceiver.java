package com.example.activitytracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


public class MyBroadcastreceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){

        String action = intent.getAction();
        if (action.equals("com.example.activitytracker.CUSTOM_INTENT")) {
            Toast.makeText(context, "Network watcher enabled", Toast.LENGTH_SHORT).show();
        }

        if (("android.net.conn.CONNECTIVITY_CHANGE").equals(action)) {
            Toast.makeText(context, "Network changed", Toast.LENGTH_SHORT).show();
        }
        }
    }

