# FusedLocationProviderDemo


Lab Google Play Services – Fused Location Provider
Objectives:

    Permissions
    Library Dependencies
    FusedLocationProviderClient
    getLastLocation

Steps:

    Make sure your emulator incorporating with Google APIs
    Make sure your SDK Tools includes Google Play Services


Preparedness:

Make sure you install Google play services.
Launch Android Studio, Go to Tools > SDK Manager
Click SDK Tools (around the middle next to SDK Platforms)
Scroll down to see if you have installed “Google Play Services”.
If not, check it and click Apply to install it.

Step 1:  Create a project, FusedLocationProviderDemo, by using empty activity template
Step 2: In build.gradle (Module:) add the following
implementation 'com.google.android.gms:play-services-location:21.0.1'
to dependencies
And click “Sync Now” on the upper right hand corner

(This may not be the latest version. But this is the version at least you can incorporate.)
(Refer to https://developers.google.com/android/guides/setup)

Step 3: In manifest add the following two permissions to access sensors for Wi-Fi/Network and GPS. Put these two permissions before <application> element:
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

Step 4: Add id element to TextView in activity_main.xml as follows:
android:id="@+id/tv"

So the TextView should look like

<TextView
android: id=”@+id/tv”
…..
……..
……… />

Step 5: Declare two variables in MainActivity.java file as follows:
static final String GooglePlayMSG = "Google Play Services are not available";
TextView tv;

Step 6: Put the following statements inside onCreate() but beneath the setContentView(…..) statement:
tv = findViewById(R.id.tv);

GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
int result = availability.isGooglePlayServicesAvailable(this);
if (result != ConnectionResult.SUCCESS){
if (!availability.isUserResolvableError(result)){
Toast.makeText(this,GooglePlayMSG, Toast.LENGTH_LONG).show();
}
} else {
Toast.makeText(this, "Get Connected", Toast.LENGTH_LONG).show();
}

Make sure you import classes if you just copy my code.
Step 7: Test/launch the app to see the “Get Connected” message in Toast dialog. If not, need to debug the app before moving on to next step. Make sure the version of your emulator matches the one defined in the manifest. You can change the emulator by clicking one on the top of the editor.
Step 8: Implement a method, updateTextView(Location), as follows:
private void updateTextView(Location location){
String latLngString = "No location found";

    if (location != null){
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latLngString = "Lat: " + lat + "\nLong: " + lng;
    }
    tv.setText(latLngString);
}

Step 9: Implement another method, getLastLocation(), as follows:

// Notice when you type PERMISSION_GRANTED you want to pick PackageManager.PERMISSION_GRANTED to import the package
//If you type the code yourself (instead of “copy and paste”), you will find the system creates the onSuccess() for you
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

// You can also see I put the <Location>() in red to gain your attention. Some people will type in as (Location)()
// Then you know what will happens. IT WON’T WORK!!!

// Notice: Make sure you set up location in Location section in Extended Controller associated to the emulator to assign Latitude and Longitude. You click on a location and then click “Set Location” button.
Step 10: Define a class variable, permission request code, as follows:
private static final int LOCATION_PERMISSION_REQUEST = 101;


Step 11: Define onStart() method as follows to request for runtime permission


protected void onStart(){

    super.onStart();



    int permission = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);



    if (permission == PERMISSION_GRANTED) {

        getLastLocation();

    } else {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},

                LOCATION_PERMISSION_REQUEST);

    }

}



Step 12: Define a method, onRequestPermissionsResult(), as follows:



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

Step 13: Declare a class variable, myLocationCallBack, as follows:
LocationCallback myLocationCallBack;

Step 14: Define a callback in onCreate()  method to update text view
myLocationCallBack = new LocationCallback(){
@Override
public void onLocationResult(LocationResult locationResult){
Location location = locationResult.getLastLocation();
if (location != null){
updateTextView(location);
}
}
};

Step 15: Define a location request variable
private LocationRequest myLocationRequest;

Make sure you import the class from google.android.gms and not from android.location
Step 16: Define a request by putting the following statements at the end of onCreate() after the code of Step 14:
myLocationRequest = new LocationRequest()
.setInterval(6000)
.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

Step 17: Define a class variable as follows:
private boolean mRequestingLocationUpdates = false;

Step 18: Put the statement to be the last statement in onStart()
mRequestingLocationUpdates = true;

Step 19: Define a method, startLocationUpdates(), as follows:
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

Step 20: Define onResume() as follows:
@Override
protected void onResume() {
super.onResume();
if (mRequestingLocationUpdates) {
startLocationUpdates();
}
}

Step 21: Now run the app and send different altitude and longitude to see the data updates on your emulator (through the Location panel in Extended controls)
Under Extended controls, select Location, set location

It may take a while for the location you set up at Step 9 to appear. Then you may want to set up a location far away to see the change on the Latitude and Longitude.

Submission: Upload couple of screens to show you can detect locations

Challenge: Put in onPause() to stop the update to save battery power
Challenge: You can combine with google map to provide geocoding, etc., and replace LocationRequest(), which is deprecated, with the latest version.

References:
Receive Location Update https://developer.android.com/training/location/receive-location-updates.html#java
https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 
