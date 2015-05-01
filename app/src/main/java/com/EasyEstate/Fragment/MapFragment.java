package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.EasyEstate.Activity.MapDisplayActivity;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * Created by canturker on 30/04/15.
 */
/*
Show the map on Listing Activity
 */
public class MapFragment extends Fragment {
    private GoogleMap googleMap;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        if(googleMap==null){
            googleMap=((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        }
        double longitude = getArguments().getDouble(MapDisplayActivity.LONGITUDE);
        double latitude = getArguments().getDouble(MapDisplayActivity.LATITUDE);
        String address = getArguments().getString(MapDisplayActivity.ADDRESS);
        setMarker(new LatLng(latitude,longitude),address);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        return view;
    }

    private void setMarker(LatLng latLng,String address){
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(address);
        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        // Placing a marker on the touched position
        googleMap.addMarker(markerOptions);
    }
}
