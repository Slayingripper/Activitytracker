package com.example.activitytracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Listme extends AppCompatActivity {
    //Context context = this;
    public static final String TAG = "Activitytracker";
    ContentResolver resolver;
    Cursor getInfo,getfastest,getoldest;
    ListView listView;
    attributeListAdapter adapter;
    private ArrayList<attributes> mAttibutesList;
    public int numberoflogs = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_listme2 );

        resolver = getContentResolver ();
        //runs the display function
        final DBhandler dbhandler = new DBhandler ( this );
        getInfo = dbhandler.getinfo ();
        Log.d ( "g53mdp", "oncreate works" );
        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter2);
        listView = findViewById ( R.id.dateList );

        mAttibutesList = new ArrayList<> (  );
//grabs the data from the database and displays into to an
//array list while . This function will grab the data as is 
//will no sorts
        while(getInfo.moveToNext () ){
            mAttibutesList.add ( new attributes (  getInfo.getString ( getInfo.getColumnIndex ( "datetime" ) ),
                    getInfo.getString ( getInfo.getColumnIndex ( "time" ) ),
                    getInfo.getString ( getInfo.getColumnIndex ( "distance" ) ) ,
                    getInfo.getString ( getInfo.getColumnIndex ( "speed" ) ) ) );

        }

        adapter = new attributeListAdapter ( getApplicationContext (), mAttibutesList );
        listView.setAdapter ( adapter );
        numberoflogs = listView.getCount();
        Log.d ( "The number of logs are", String.valueOf ( numberoflogs ) );

        listView.setOnItemLongClickListener ( new AdapterView.OnItemLongClickListener () {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder ( Listme.this);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Delete item?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        attributes temp = (attributes) listView.getItemAtPosition ( position );
                        String tempString = temp.getTime ();

                        dbhandler.removeSinglerow ( tempString );
                        //Update your ArrayList
                      //   mAttibutesList = getInfo();
                        //Notify your ListView adapter
                        mAttibutesList.remove ( mAttibutesList.get ( position ) );
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Listme.this, "Item Deleted", Toast.LENGTH_LONG).show();

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return false;
            }
        } );

    }
    
    // we use a drop down spinner to query the database witb questions 
    
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

        switch (position) {
            case 0:
                while(getInfo.moveToNext () ){
                    mAttibutesList.add ( new attributes (  getInfo.getString ( getInfo.getColumnIndex ( "datetime" ) ),
                            getInfo.getString ( getInfo.getColumnIndex ( "time" ) ),
                            getInfo.getString ( getInfo.getColumnIndex ( "distance" ) ) ,
                            getInfo.getString ( getInfo.getColumnIndex ( "speed" ) ) ) );
                }
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
            while    (getfastest.moveToNext () ){
                mAttibutesList.add ( new attributes (  getfastest.getString ( getfastest.getColumnIndex ( "datetime" ) ),
                        getfastest.getString ( getfastest.getColumnIndex ( "time" ) ),
                        getfastest.getString ( getfastest.getColumnIndex ( "distance" ) ) ,
                        getfastest.getString ( getfastest.getColumnIndex ( "speed" ) ) ) );
            }
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                while    (getoldest.moveToNext () ){
                    mAttibutesList.add ( new attributes (  getoldest.getString ( getoldest.getColumnIndex ( "datetime" ) ),
                            getoldest.getString ( getoldest.getColumnIndex ( "time" ) ),
                            getoldest.getString ( getoldest.getColumnIndex ( "distance" ) ) ,
                            getoldest.getString ( getoldest.getColumnIndex ( "speed" ) ) ) );
                }
                // Whatever you want to happen when the thri item gets selected
                break;

            default:
                throw new IllegalStateException ( "Unexpected value: " + position );
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
