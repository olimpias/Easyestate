package com.EasyEstate.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Model.ListingLocation;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by canturker on 17/04/15.
 */
public class InsertListingMapFragment extends Fragment {
    private static final String TAG = "INSERT_LISTING_MAP";
    private GoogleMap googleMap;
    private Button nextButton;
    private ListingLocation location;
    private SeekBar seekBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_map_fragment, container, false);
        if(googleMap==null){
             googleMap=((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        }
        seekBar = (SeekBar)view.findViewById(R.id.seekBar3);
        seekBar.setEnabled(false);
        nextButton = (Button)view.findViewById(R.id.nextMapButton);
        GpsTracker tracker = new GpsTracker(getActivity());
        if(MyListingControlActivity.isEditing()){
                double latitude = MyListingControlActivity.getListing().getLocation().getLatitude();
                double longitude = MyListingControlActivity.getListing().getLocation().getLongitude();
                setMarker(new LatLng(latitude,longitude));
                setLocationFromAddress(longitude,latitude);
        }else{
            if(tracker.getLocation() != null){
                setMarker(new LatLng(tracker.getLatitude(),tracker.getLongitude()));
                setLocationFromAddress(tracker.getLongitude(),tracker.getLatitude());
            }
        }

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               setMarker(latLng);
            }
        });
        nextButton.setOnClickListener(buttonListener);
        return view;
    }
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                  if(v.getId() == nextButton.getId()){
                      MyListingControlActivity.getListing().setLocation(location);
                      ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertImageFragment());
                  }

        }
    };
    private void setMarker(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title("Your Listing Place");
        setLocationFromAddress(latLng.longitude,latLng.latitude);
        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        // Placing a marker on the touched position
        googleMap.addMarker(markerOptions);
    }
    private void setLocationFromAddress(double longitude,double latitude){
        location = new ListingLocation();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        List<Address> addressesList;
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            if (geocoder.isPresent()) {
                addressesList = geocoder.getFromLocation(latitude, longitude, 1);
                Address address=addressesList.get(0);
                String description = "";
                for(int i = 0;i<address.getMaxAddressLineIndex();i++){
                    description = description+" "+address.getAddressLine(i);
                }
                location.setAddress(description.toLowerCase());
                Log.d(TAG,description);
            }
        }catch (IOException ex){

        }
    }

}
