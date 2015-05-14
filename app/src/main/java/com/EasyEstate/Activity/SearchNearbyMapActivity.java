package com.EasyEstate.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.GpsTracker;
import com.EasyEstate.SupportTool.ListingImage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class SearchNearbyMapActivity extends ActionBarActivity {
    private GoogleMap googleMap;
    private HashMap<Marker,ListingImage> markerListingHashMap;
    private  String [] array;
    private GpsTracker gpsTracker;
    private static final String TAG = "MAP";
    private double minLong;
    private double maxLong;
    private double minLat;
    private double maxLat;
   // 1 km
    private static final double KM = 0.009;
    private static final double DISTANCE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nearby_map);
        if(googleMap == null){
            googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdaptor());
        googleMap.setOnMarkerClickListener(onMarkerClickListener);
        googleMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
        markerListingHashMap = new HashMap<>();
        array = getResources().getStringArray(R.array.planets_array);
        gpsTracker = new GpsTracker(this);
        Location location = gpsTracker.getLocation();
        CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(10).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        EstimateMinMaxBounds(location);
    }
    private void EstimateMinMaxBounds(Location location){
        minLat = location.getLatitude() - KM * DISTANCE;
        maxLat = location.getLatitude() + KM * DISTANCE;
        minLong = location.getLongitude() - ((KM * DISTANCE)/Math.cos(location.getLatitude()*Math.PI/180));
        maxLong = location.getLongitude() + ((KM * DISTANCE)/Math.cos(location.getLatitude()*Math.PI/180));
        new NetworkConnection().execute();
    }
    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.showInfoWindow();
            return false;
        }
    };
    private GoogleMap.OnInfoWindowClickListener onInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Listing listing = markerListingHashMap.get(marker).getListing();
            Intent intent = new Intent(SearchNearbyMapActivity.this,ListingActivity.class);
            intent.putExtra(ListingActivity.AD_ID,listing.getAdID());
            String type;
            if (listing instanceof House){
                type ="0";
            }else{
                type = "1";
            }
            intent.putExtra(ListingActivity.AD_TYPE,type);
            startActivity(intent);
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private class MarkerInfoWindowAdaptor implements  GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v=getLayoutInflater().inflate(R.layout.listing_map_marker, null);
            ListingImage listingImage=markerListingHashMap.get(marker);
            TextView addressTextView = (TextView)v.findViewById(R.id.addressTextView);
            ImageView imageView = (ImageView)v.findViewById(R.id.listingImageButton);
            TextView informationTextView = (TextView)v.findViewById(R.id.informationTextView);
            TextView priceTextView = (TextView)v.findViewById(R.id.priceTextView);
            TextView titleTextView = (TextView)v.findViewById(R.id.titleTextView);
            addressTextView.setText(listingImage.getListing().getLocation().getAddress());
            priceTextView.setText(listingImage.getListing().getPrice()+"");
            titleTextView.setText(listingImage.getListing().getTitle());
            if(listingImage.getListing() instanceof House){
                informationTextView.setText(array[((House)listingImage.getListing()).getNumberOfRoom()]);
            }
            if(listingImage.getBitmap() != null){
                imageView.setImageBitmap(listingImage.getBitmap());
            }


            return v;
        }

    }
    private class NetworkConnection extends AsyncTask<Void,Void,List<Listing>> {
        @Override
        protected List<Listing> doInBackground(Void... params) {

            try {
                return DatabaseConnection.getConnection().selectNearbySearchQuery(minLat,maxLat,minLong,maxLong);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Listing> listings) {
            super.onPostExecute(listings);
            if(listings != null){

                markerListingHashMap.clear();
                googleMap.clear();
                for(int i = 0;i<listings.size();i++){
                    Listing listing = listings.get(i);
                    ListingImage listingImage = new ListingImage(listing);
                    if(listing.getImagesURL()!=null && listing.getImagesURL().size()!= 0){
                        new LoadImage().execute(listingImage);
                    }
                    MarkerOptions markerOption;
                    LatLng latLng = new LatLng(listing.getLocation().getLatitude(), listing.getLocation().getLongitude());

                    if(listing instanceof House)
                        markerOption =new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    else
                        markerOption =new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    Marker marker = googleMap.addMarker(markerOption);
                    markerListingHashMap.put(marker,listingImage);
                }

            }
        }
        private class LoadImage extends AsyncTask<ListingImage,Void,Bitmap> {
            ListingImage imageLoad;
            @Override
            protected Bitmap doInBackground(ListingImage... params) {
                this.imageLoad = params[0];
                try {
                    return BitmapTool.newScaledThumbnailBitmap(new URL(imageLoad.getUrl()).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(bitmap!=null){
                    Log.i(TAG, "Image Uploaded");
                    imageLoad.setBitmap(bitmap);
                }
            }

        }
    }




}
