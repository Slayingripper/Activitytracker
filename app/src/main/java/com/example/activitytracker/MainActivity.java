package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import org.shredzone.commons.suncalc.SunTimes;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button listbtn,startbtn,stopbtn;
    Date date = Calendar.getInstance().getTime(); // date of calculation
    //for nottingham
    double lat= 52.9536000;
    double lng = -1.1504700 ;// geolocation
    public static final String TAG = "ActivityMain";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnalogClock simpleAnalogClock = (AnalogClock)findViewById(R.id.analogClock);
        DigitalClock simpleDigitalClock = (DigitalClock) findViewById(R.id.digitalClock);
        listbtn = findViewById(R.id.listbtn);
        startbtn = findViewById(R.id.startbtn);
        stopbtn = findViewById(R.id.stopbtn);
        Log.d(TAG, "Buttons created");


    }
// opens the list view showin the database 
public void onlistbtnclick (View view){
    Intent intent = new Intent(this, Listme.class);
    startActivity(intent);
    Log.d(TAG, "Listing OPENED");

}
//starts the workout and the service 
public  void onstartclick (View view){
    Intent intent = new Intent(this, MapsActivity.class);
    startActivity(intent);
    Log.d(TAG, "Listing OPENED");
}
//kills the process and the location provider 
public  void onexit (View view) {
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(1);
}
public void onsun(View view){
    calculatesun();
}
public void calculatesun(){
        SunTimes times = SunTimes.compute()
            .on(date)       // set a date
            .at(lat, lng)   // set a location
            .execute();     // get the results
    System.out.println("Sunrise: " + times.getRise());
    System.out.println("Sunset: " + times.getSet());
    Toast.makeText(MainActivity.this,
            "Sunrise:" + times.getRise() + " Sunset:" + times.getSet(),
            Toast.LENGTH_SHORT).show();

}

}
