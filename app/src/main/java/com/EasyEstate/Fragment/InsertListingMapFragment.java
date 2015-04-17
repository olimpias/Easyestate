package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.EasyEstate.Model.ListingLocation;
import com.EasyEstate.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by canturker on 17/04/15.
 */
public class InsertListingMapFragment extends android.app.Fragment {
    private GoogleMap googleMap;
    private Button findLocationButton;
    private Button nextButton;
    private ListingLocation location;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_map_fragment,container,false);
        if(googleMap==null){
            googleMap=((MapFragment)getFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        }
        findLocationButton = (Button)view.findViewById(R.id.GPSButton);
        nextButton = (Button)view.findViewById(R.id.nextMapButton);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        nextButton.setOnClickListener(buttonListener);
        findLocationButton.setOnClickListener(buttonListener);
        return view;
    }
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                  if(v.getId() == nextButton.getId()){

                  }
                  if(v.getId() == findLocationButton.getId()){

                  }
        }
    };

}
