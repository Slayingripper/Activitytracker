package com.example.activitytracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class attributeListAdapter extends BaseAdapter {

    private Context mcontext;
    private ArrayList<attributes> mAttributesList;

    //constructor
     public attributeListAdapter(Context mcontext, ArrayList<attributes> mAttributesList) {
        this.mcontext = mcontext;
        this.mAttributesList = mAttributesList;
    }

    @Override
    public int getCount() {
        return mAttributesList.size ();
    }

    @Override
    public Object getItem(int position) {
        return mAttributesList.get ( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
//sets the values of the list adapter
//and handles how they will be displayed

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView ==  null){
            convertView = View.inflate ( mcontext, R.layout.list_attributes, null );
        }

        TextView dateTime = convertView.findViewById ( R.id.dateTimeText );
        TextView distance = convertView.findViewById ( R.id.distanceText );
        TextView speed = convertView.findViewById ( R.id.speedText );
        TextView time = convertView.findViewById ( R.id.timeText );
//handles the contents that is displayed in the text views

        final attributes mattributes = mAttributesList.get ( position );
       dateTime.setText(  mattributes.getDateTime ()  );
       time.setText (  mattributes.getTime ()   );
       distance.setText (  mattributes.getDistance ()   );
       speed.setText (  mattributes.getSpeed ()   );


       return convertView;

    }
}
