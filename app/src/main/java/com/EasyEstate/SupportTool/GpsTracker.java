package com.EasyEstate.SupportTool;

        import android.app.AlertDialog;
        import android.app.Service;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.IBinder;
        import android.util.Log;

/**
 * Created by canturker on 18/02/15.
 */
public class GpsTracker extends Service implements LocationListener {
    private final Context context;

    boolean isGPSEnable= false;
    boolean isNetworkEnable= false;
    boolean canGetLocation= false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATE = 1000*60*1;
    private Location location;
    private double latitude; //Enlem
    private double longitude; //Boylam
    protected LocationManager locationManager;
    public GpsTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        locationManager=(LocationManager)context.getSystemService(LOCATION_SERVICE);
        isNetworkEnable=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isNetworkEnable){
             ShowSettingsAlert();
        }else{
            this.canGetLocation=true;
            if(isNetworkEnable){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATE,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                if(locationManager!=null){
                    location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!=null){
                        Log.e("Latitude Value",location.getLatitude()+"");
                        this.latitude=location.getLatitude();
                        this.longitude=location.getLongitude();
                    }
                }
            }

        }
        return location;
    }
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager!=null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
    /*
    Returns Latitude value
     */
    public double getLatitude() {
        if(location!=null){
            latitude=location.getLatitude();
        }
        return latitude;
    }
    /*
    Returns Longitude value
     */
    public double getLongitude(){
        if(location!=null){
            longitude=location.getLongitude();
        }
        return longitude;
    }
    /*
    Returns CanGetLocation values
     */
    public boolean isCanGetLocation() {
        return canGetLocation;
    }
    public void ShowSettingsAlert(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("You don't have internet connection");
        alertDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
