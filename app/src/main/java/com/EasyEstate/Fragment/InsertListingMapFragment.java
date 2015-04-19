package com.EasyEstate.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.EasyEstate.Model.ListingLocation;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by canturker on 17/04/15.
 */
public class InsertListingMapFragment extends android.app.Fragment {
    private GoogleMap googleMap;
    private Button nextButton;
    private ListingLocation location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_map_fragment,container,false);
        if(googleMap==null){
            googleMap=((MapFragment)getFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        }
        nextButton = (Button)view.findViewById(R.id.nextMapButton);
        GpsTracker tracker = new GpsTracker(getActivity());
        if(tracker.getLocation() != null){
            setMarker(new LatLng(tracker.getLatitude(),tracker.getLongitude()));
            setLocationFromAddress(tracker.getLongitude(),tracker.getLatitude());

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
                        // next
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

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
                location.setAddress(description);
            }
        }catch (IOException ex){

        }
    }

}
