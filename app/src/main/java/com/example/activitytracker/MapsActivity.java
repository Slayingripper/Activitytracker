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

import java.text.SimpleDateFormat;

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


              //  MapsActivity.this.finishActivity ( 1 );
               // MapsActivity.super.onDestroy ();
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

            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
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

                            Toast.makeText(MapsActivity.this,
                                    "latitude:" + firstlat + " longitude:" + firstlong,
                                    Toast.LENGTH_SHORT).show();



                            //get the latitude
                             latitude = location.getLatitude ();
                            //get the longitude
                             longitude = location.getLongitude ();
                            //here we are instantiating the class , latlng
                            LatLng latLng = new LatLng ( latitude, longitude );
                            //this can be utilised only if the GPS provider is working


                            //we use Geocoder to convert the values of latlng
                            Geocoder geocoder = new Geocoder ( getApplicationContext () );
                            /*try {
                                geocoder.getFromLocation ( firstlat ,firstlat,1);
                            } catch (IOException e) {
                                e.printStackTrace ();
                            }*/

                            try {
                                List<Address> addressList = geocoder.getFromLocation ( latitude, longitude, 1 );

                                // mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                mMap.getUiSettings ().setZoomControlsEnabled ( true );
                                CircleOptions circleOptions = new CircleOptions ();
                                circleOptions.center ( new LatLng ( location.getLatitude (),
                                        location.getLongitude () ) );
                                circleOptions.radius ( 30 );
                                circleOptions.fillColor ( Color.BLUE );
                                circleOptions.strokeWidth ( 6 );

                                mMap.addCircle ( circleOptions );


                                mMap.moveCamera ( CameraUpdateFactory.newLatLngZoom ( latLng, 14.0f ) );
                           /*     dis = biglocation.distanceTo ( location );
                                biglocation = location;
                                distance = dis + distance;
                                Log.d ( "GOT THE DISTANCE", String.valueOf ( distance ) );*/

                            } catch (IOException e) {
                                e.printStackTrace ();
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Toast.makeText(MapsActivity.this,
                                    "latitude:" + firstlat + " longitude:" + firstlong,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProviderEnabled(String provider) {

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

                            /*locationInfo = location;
                            locationA = new Location( String.valueOf ( provider ) );
                            try {
                                locationA = locationManager.getLastKnownLocation( String.valueOf ( provider ) );
                            } catch(SecurityException e) {
                                e.printStackTrace();
                            }
                            locationInfo = location;

                            iTrackPoint++;
                            try {
                                distance = location.distanceTo(locationA) / 1000;
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }*/
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
                                mMap.getUiSettings ().setZoomControlsEnabled ( true );
                                CircleOptions circleOptions = new CircleOptions ();
                                circleOptions.center ( new LatLng ( location.getLatitude (),
                                        location.getLongitude () ) );

                                circleOptions.radius ( 30 );
                                circleOptions.fillColor ( Color.BLUE );
                                circleOptions.strokeWidth ( 6 );

                                mMap.addCircle ( circleOptions );

                                mMap.moveCamera ( CameraUpdateFactory.newLatLngZoom ( latLng, 15.0f ) );

                            } catch (IOException e) {
                                e.printStackTrace ();
                            }


                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Toast.makeText(MapsActivity.this,
                                    "Tracking started using Network",
                                    Toast.LENGTH_SHORT).show();
                        }

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
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void adddata(Location location) {
        long timetaken = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
         //  timetaken = startTime;
            endTime = System.currentTimeMillis ();
            timetaken = preference.getLong("TotalForegroundTime", 0) + (endTime-startTime);
            timetaken = TimeUnit.SECONDS.convert(timetaken,TimeUnit.MILLISECONDS);

        }
        DBhandler dbhandler = new DBhandler ( getBaseContext () );
        Log.d ( "g53mdp", "submit success" );
        String currentDateandTime = sdf.format ( new Date () );
        //String currentDateandTime = ("16/10/20");
        Log.d ( "g53mdp", "DATE" );
        int time1 = (int) timetaken;
        float calculatedspeed = dis / time1;
       double distance1 = (double)Math.round(distance * 1d) / 1000d;
        String distancetrue = String.valueOf ( distance1 );
       // String distancetrue = "432092";
        Log.d ( "time", String.valueOf ( time1 ) );
        String time = Integer.toString ( time1 );
        //String newtime = removeLastChar ( time );
        String speed = Float.toString ( calculatedspeed );
        Sport sport = new Sport ( currentDateandTime, distancetrue,speed,time );
        dbhandler.addLog ( sport );
        Toast.makeText(MapsActivity.this, "Activity Logged", Toast.LENGTH_LONG).show();
        Log.d ( "GOT THE DISTANCE", String.valueOf ( distance ) );
        this.finishAffinity();

       // super.onDestroy ();
        // distanceTo ( location );
    }

    public static String removeLastChar(String s) {
        return (s == null || s.length () == 10)
                ? null
                : (s.substring ( 0, s.length () + 1 ));
    }





   /* public void distanceTo(Location location) {
        float distance;
        //get the latitude

        double latitude2 = locationInfo.getLatitude ();
        //get the longitude
        double longitude2 = locationInfo.getLongitude ();

        Location locationB = new Location("point B");

        locationB.setLatitude(latitude2);
        locationB.setLongitude(longitude2);


        Geocoder geocoder = new Geocoder ( getApplicationContext () );
        try {
            geocoder.getFromLocation ( latitude2, longitude2, 1 );
        } catch (IOException e) {
            e.printStackTrace ();
        }
        float results[] = new float[10];
        location.distanceBetween ( firstlong,firstlat,longitude2,latitude2,results);

    }
*/
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
