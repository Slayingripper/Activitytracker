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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Listme extends AppCompatActivity {
    //Context context = this;
    public static final String TAG = "Activitytracker";
    ContentResolver resolver;
    Cursor getInfo;
    ListView listView;
    attributeListAdapter adapter;
    private ArrayList<attributes> mAttibutesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_listme2 );

        resolver = getContentResolver ();
        //runs the display function
        final DBhandler dbhandler = new DBhandler ( this );
        getInfo = dbhandler.getinfo ();
        Log.d ( "g53mdp", "oncreate works" );

        listView = findViewById ( R.id.dateList );

        mAttibutesList = new ArrayList<> (  );

        while(getInfo.moveToNext () ){
            mAttibutesList.add ( new attributes (  getInfo.getString ( getInfo.getColumnIndex ( "datetime" ) ),
                    getInfo.getString ( getInfo.getColumnIndex ( "time" ) ),
                    getInfo.getString ( getInfo.getColumnIndex ( "distance" ) ) ,
                    getInfo.getString ( getInfo.getColumnIndex ( "speed" ) ) ) );
        }

        adapter = new attributeListAdapter ( getApplicationContext (), mAttibutesList );
        listView.setAdapter ( adapter );


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

}
