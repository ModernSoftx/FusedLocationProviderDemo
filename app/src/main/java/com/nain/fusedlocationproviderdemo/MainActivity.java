package com.nain.fusedlocationproviderdemo;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    static final String GooglePlayMSG = "Google Play Services are not available";
    TextView tv;
    private static final int LOCATION_PERMISSION_REQUEST = 101;
    LocationCallback myLocationCallBack;
    private LocationRequest myLocationRequest;
    private boolean mRequestingLocationUpdates = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        myLocationCallBack = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                Location location = locationResult.getLastLocation();
                if (location != null){
                    updateTextView(location);
                }
            }
        };

        myLocationRequest = new LocationRequest()
                .setInterval(6000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int result = availability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS){
            if (!availability.isUserResolvableError(result)){
                Toast.makeText(this,GooglePlayMSG, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Get Connected", Toast.LENGTH_LONG).show();
        }

    }

    private void updateTextView(Location location){
        String latLngString = "No location found";

        if (location != null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLngString = "Lat: " + lat + "\nLong: " + lng;
        }
        tv.setText(latLngString);
    }

    private void getLastLocation(){
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED)
        ) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            updateTextView(location);
                        }
                    });
        }
    }

    protected void onStart(){

        super.onStart();



        int permission = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);



        if (permission == PERMISSION_GRANTED) {

            getLastLocation();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},

                    LOCATION_PERMISSION_REQUEST);

        }
        mRequestingLocationUpdates = true;

    }

    private void startLocationUpdates() {

        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED)
        ) {
            fusedLocationClient.requestLocationUpdates(myLocationRequest,
                    myLocationCallBack,
                    null /* Looper */);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST){
            if (grantResults[0] != PERMISSION_GRANTED)
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_LONG).show();
            else
                getLastLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
}