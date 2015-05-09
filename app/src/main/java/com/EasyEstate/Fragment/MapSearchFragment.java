package com.EasyEstate.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.EasyEstate.Activity.ListingActivity;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.ListingImage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
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

/**
 * Created by canturker on 08/05/15.
 */
public class MapSearchFragment extends Fragment {
    private GoogleMap googleMap;
    private String query = null;
    private String queryDetails = null;
    private HashMap<Marker,ListingImage> markerListingHashMap;
    private  String [] array;
    private static final String TAG = "MAP";
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        if(googleMap==null){
            googleMap=((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        }
        query = getArguments().getString(SearchFragment.QUERY);
        queryDetails = getArguments().getString(SearchFragment.QUERY_DETAILS);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdaptor());
        googleMap.setOnMarkerClickListener(onMarkerClickListener);
        markerListingHashMap = new HashMap<>();
        googleMap.setOnInfoWindowClickListener(onInfoWindowClickListener);
        array = getActivity().getResources().getStringArray(R.array.planets_array);
        new NetworkConnection().execute();
        return view;
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
            Intent intent = new Intent(getActivity(),ListingActivity.class);
            intent.putExtra(ListingActivity.AD_ID,listing.getAdID());
            String type;
            if (listing instanceof House){
                type ="0";
            }else{
                type = "1";
            }
            intent.putExtra(ListingActivity.AD_TYPE,type);
            getActivity().startActivity(intent);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class NetworkConnection extends AsyncTask<Void,Void,List<Listing>> {
        private int size;
        @Override
        protected List<Listing> doInBackground(Void... params) {

            try {
                return DatabaseConnection.getConnection().selectSearchQueryMap(query,queryDetails);
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
                    if(i == 0){
                        CameraPosition cameraPosition=new CameraPosition.Builder().target(latLng).zoom(12).build();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                    if(listing instanceof House)
                        markerOption =new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    else
                        markerOption =new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    Marker marker = googleMap.addMarker(markerOption);
                    markerListingHashMap.put(marker,listingImage);
                }

            }
        }
    }
    private class MarkerInfoWindowAdaptor implements  GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v=getActivity().getLayoutInflater().inflate(R.layout.listing_map_marker, null);
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
