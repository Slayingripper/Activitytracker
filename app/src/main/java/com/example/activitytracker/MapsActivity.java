package com.example.activitytracker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    SimpleDateFormat sdf = new SimpleDateFormat ( "dd/MM/yyyy", Locale.getDefault () );
    DBhandler dbhandler;
    SQLiteDatabase db;
    Button stopbtn;
    float[] results = new float[3];
    double distance ;
    float dis;
    int provider = 0;
    SharedPreferences preference;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    long startTime = 0, endTime = 0;
    Double firstlat = 0.0 , firstlong = 0.0;
    Double latitude = 0.0 , longitude = 0.0;
    Location location ;
    public static Location locationInfo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps );
        stopbtn = findViewById ( R.id.stopbtn );
       // startTime =SystemClock.elapsedRealtime ();
        requestLocationPermission();
        startTime = System.currentTimeMillis();
        preference = getSharedPreferences("TimeWrap", Context.MODE_PRIVATE);
        stopbtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Location.distanceBetween ( firstlat,firstlong,latitude,longitude, results );
                dis = results[0];
                distance = (double) dis;
                Intent intent = new Intent ( getApplication (), MainActivity.class );
                adddata ( locationInfo );
                startActivity(intent);

                MapsActivity.super.stopService ( intent );
                MapsActivity.super.onDestroy ();

            }
        } );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );
        locationManager = (LocationManager) getSystemService ( LOCATION_SERVICE );
        if (checkSelfPermission ( Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission ( Manifest.permission.ACCESS_COARSE_LOCATION )
                        != PackageManager.PERMISSION_GRANTED) {
            //
            return;


        }

        //we are checking if the provider is available
        if (locationManager.isProviderEnabled ( LocationManager.GPS_PROVIDER )) {
            locationManager.requestLocationUpdates

                    ( LocationManager.GPS_PROVIDER, 0, 0, new LocationListener () {


                        @Override
                        public void onLocationChanged(Location location) {
                            if (firstlong == 0.0) {
                                firstlong = location.getLongitude ();
                            }

                          else {
                                if (firstlat == 0.0) {
                                    firstlat = location.getLatitude ();
                                }
                            }



/*
                            Toast.makeText(MapsActivity.this,
                                    "latitude:" + firstlat + " longitude:" + firstlong,
                                    Toast.LENGTH_SHORT).show();*/

                            //get the latitude
                             latitude = location.getLatitude ();
                            //get the longitude
                             longitude = location.getLongitude ();
                            //here we are instantiating the class , latlng
                            LatLng latLng = new LatLng ( latitude, longitude );
                            //this can be utilised only if the GPS provider is working


                            //we use Geocoder to convert the values of latlng
                            Geocoder geocoder = new Geocoder ( getApplicationContext () );

                            try {
                                List<Address> addressList = geocoder.getFromLocation ( latitude, longitude, 1 );

                                // mMap.addMarker(new MarkerOptions().position(latLng).title(str));

                                CircleOptions circleOptions = new CircleOptions ();
                                circleOptions.center ( new LatLng ( location.getLatitude (),
                                        location.getLongitude () ) );

                                circleOptions.radius ( 30 );
                                circleOptions.fillColor ( Color.BLUE );
                                circleOptions.strokeWidth ( 6 );

                                mMap.addCircle ( circleOptions );

                                mMap.moveCamera ( CameraUpdateFactory.newLatLngZoom ( latLng, 14.0f ) );


                            } catch (IOException e) {
                                e.printStackTrace ();
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            //check the live speed of the user

                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            /*Toast.makeText(MapsActivity.this,
                                    "latitude:" + firstlat + " longitude:" + firstlong,
                                    Toast.LENGTH_SHORT).show();*/
                            //check the live speed of the user
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Toast.makeText(MapsActivity.this,
                                    "Tracking stopped using GPS",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } );
        } else if (locationManager.isProviderEnabled ( LocationManager.NETWORK_PROVIDER )) {
            locationManager.requestLocationUpdates
                    ( LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener () {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (firstlong == 0.0) {
                                firstlong = location.getLongitude ();
                            }
//we log the first position of the user
                            else {
                                if (firstlat == 0.0) {
                                    firstlat = location.getLatitude ();
                                }
                            }


                            //check the live speed of the user
                         //   speedchecklive();


                            //get the latitude
                            double latitude = location.getLatitude ();
                            //get the longitude
                            double longitude = location.getLongitude ();

                            //this can be utilised only if the GPS provider is working
                            LatLng latLng = new LatLng ( latitude, longitude );
                            //this can be utilised only if the GPS provider is working

                            //we use Geocoder to convert the valus of latlng
                            Geocoder geocoder = new Geocoder ( getApplicationContext () );

                            try {
                                List<Address> addressList = geocoder.getFromLocation ( latitude, longitude, 1 );
                                String str = addressList.get ( 0 ).getLocality () + ",";
                                str += addressList.get ( 0 ).getCountryName ();
                                // mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                           
                                //creates a circle that shows the users position

                                CircleOptions circleOptions = new CircleOptions ();
                                circleOptions.center ( new LatLng ( location.getLatitude (),
                                        location.getLongitude () ) );

                                circleOptions.radius ( 30 );
                                circleOptions.fillColor ( Color.BLUE );
                                circleOptions.strokeWidth ( 6 );

                                mMap.addCircle ( circleOptions );

                                mMap.moveCamera ( CameraUpdateFactory.newLatLngZoom ( latLng, 14.0f ) );

                            } catch (IOException e) {
                                e.printStackTrace ();
                            }


                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }
//notify the user when the provide r is enabled
                        @Override
                        public void onProviderEnabled(String provider) {
                            Toast.makeText(MapsActivity.this,
                                    "Tracking started using Network",
                                    Toast.LENGTH_SHORT).show();
                        }
//notify the user when the  provider is disabled
                        @Override
                        public void onProviderDisabled(String provider) {
                            Toast.makeText(MapsActivity.this,
                                    "Tracking stopped using Network",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    );
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.isIndoorEnabled ();
        mMap.isBuildingsEnabled ();
        mMap.getUiSettings ().setZoomControlsEnabled ( true );



    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void adddata(Location location) {
    	//here we calculate the time by measuring the time taken from when the  user starts the activity and until they
       //log it 
        long timetaken = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
         //  timetaken = startTime;
            endTime = System.currentTimeMillis ();
            timetaken = preference.getLong("TotalForegroundTime", 0) + (endTime-startTime);
            timetaken = TimeUnit.SECONDS.convert(timetaken,TimeUnit.MILLISECONDS);

        }
        // we call the formating of the  Date that we defined above
        // and log the date using the system time
        // we calculate the speed using distance/time and use the 
        //math function to convert it to 0.000 km since the data is in meters
        
        DBhandler dbhandler = new DBhandler ( getBaseContext () );
        Log.d ( "g53mdp", "submit success" );
        String currentDateandTime = sdf.format ( new Date () );
        //String currentDateandTime = ("16/10/20");
        Log.d ( "g53mdp", "DATE" );
        int time1 = (int) timetaken;
        float calculatedspeed = dis / time1;


        if (calculatedspeed > 8 ){
            Toast.makeText(MapsActivity.this,
                    "That was a god Run",
                    Toast.LENGTH_SHORT).show();
        }

        else {
            if (calculatedspeed > 30){
                Toast.makeText(MapsActivity.this,
                        "That was a good cycle",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                if (calculatedspeed > 100){
                    Toast.makeText(MapsActivity.this,
                            "Using a car wont get you any slimmer",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
speedchecklive();

       double distance1 = (double)Math.round(distance * 1d) / 1000d;
        String distancetrue = String.valueOf ( distance1 );

        Log.d ( "time", String.valueOf ( time1 ) );
        String time = Integer.toString ( time1 );

        String speed = Float.toString ( calculatedspeed );
        Sport sport = new Sport ( currentDateandTime, distancetrue,speed,time );
        dbhandler.addLog ( sport );
        Toast.makeText(MapsActivity.this, "Activity Logged", Toast.LENGTH_LONG).show();
        Log.d ( "GOT THE DISTANCE", String.valueOf ( distance ) );
        this.finishAffinity();

       // super.onDestroy ();
        // distanceTo ( location );
    }
//we use this function if we want to remove characters from a string
    public static String removeLastChar(String s) {
        return (s == null || s.length () == 10)
                ? null
                : (s.substring ( 0, s.length () + 1 ));
    }

public  void speedchecklive(){
        Float speedo = 0.0f;
     speedo = location.getSpeed();

    if (speedo > 8 ){
        Toast.makeText(MapsActivity.this,
                "Started Running",
                Toast.LENGTH_SHORT).show();
    }

    else {
        if (speedo > 30){
            Toast.makeText(MapsActivity.this,
                    "Started Cycling",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            if (speedo> 100){
                Toast.makeText(MapsActivity.this,
                        "Get out of the car",
                        Toast.LENGTH_SHORT).show();
            }
            else if (speedo == 0){
                Toast.makeText(MapsActivity.this,
                        "Start Moving",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}





//Check whether location permisions are turned on
//if not pops up a window to enable them if enabled will show a toast notification
   @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);

       // Forward results to EasyPermissions
       EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
   }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
}

}
