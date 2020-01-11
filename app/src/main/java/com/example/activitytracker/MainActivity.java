package com.example.activitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Button listbtn,startbtn,stopbtn;

    public static final String TAG = "ActivityMain";
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listbtn = findViewById(R.id.listbtn);
        startbtn = findViewById(R.id.startbtn);
        stopbtn = findViewById(R.id.stopbtn);
        Log.d(TAG, "Buttons created");

        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);



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
public  void onexit (View view){
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(1);
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
}

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
