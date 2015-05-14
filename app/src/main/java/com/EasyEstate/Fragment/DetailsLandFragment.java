package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;

/**
 * Created by canturker on 30/04/15.
 */
public class DetailsLandFragment extends Fragment {
    private Listing listing;
    private TextView descriptionTextView;
    private TextView sizeTextView;
    private TextView estateTypeTextView;
    private TextView zoningStatusTextView;
    private TextView islandNoTextView;
    private TextView layoutNoTextView;
    private TextView gabariTextView;
    private TextView deedStatusTextView;
    private TextView provisionFloorTextView;
    private TextView loanEligibilityTextView;
    private TextView parcelNOTextView;
    private String [] gabariTypes;
    private String []deedStatusTypes;
    private String [] zoningStatusTypes;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_land_fragment, container, false);
        descriptionTextView = (TextView)view.findViewById(R.id.descriptionTextView);
        sizeTextView = (TextView)view.findViewById(R.id.sizeTextView);
        estateTypeTextView = (TextView)view.findViewById(R.id.estateTypeTextView);
        zoningStatusTextView = (TextView)view.findViewById(R.id.zoningStatusTextView);
        islandNoTextView = (TextView)view.findViewById(R.id.islandNoTextView);
        layoutNoTextView = (TextView)view.findViewById(R.id.layoutNoTextView);
        gabariTextView = (TextView)view.findViewById(R.id.gabariTextView);
        deedStatusTextView = (TextView)view.findViewById(R.id.deedStatusTextView);
        provisionFloorTextView = (TextView)view.findViewById(R.id.provisionFloorTextView);
        loanEligibilityTextView = (TextView)view.findViewById(R.id.loanEligibilityTextView);
        parcelNOTextView = (TextView)view.findViewById(R.id.parcelNOTextView);
        gabariTypes = getResources().getStringArray(R.array.gabari_array);
        deedStatusTypes = getResources().getStringArray(R.array.deed_status_array);
        zoningStatusTypes = getResources().getStringArray(R.array.zoning_status_array);
        return view;
    }
    public void ListingDataChanged(Listing listing){
        setListing(listing);
        //Add Values to views.
        Land land = (Land)listing;
        descriptionTextView.setText(land.getDescription());
        sizeTextView.setText(land.getSquareMeter()+"m");
        estateTypeTextView.setText(land.getEstateType());
        zoningStatusTextView.setText(zoningStatusTypes[land.getZoningStatus()]);
        gabariTextView.setText(gabariTypes[land.getGabari()]);
        deedStatusTextView.setText(deedStatusTypes[land.getDeedStatus()]);
        islandNoTextView.setText(land.getIslandNo()+"");
        layoutNoTextView.setText(land.getLayoutNo()+"");
        parcelNOTextView.setText(land.getParcelNo()+"");
        if(land.isProvisionFloor()){
            provisionFloorTextView.setText("Yes");
        }else{
            provisionFloorTextView.setText("No");
        }
        if(land.isLoanEligibility()){
            loanEligibilityTextView.setText("Yes");
        }else{
            loanEligibilityTextView.setText("Yes");
        }

    }
    private void setListing(Listing listing) {
        this.listing = listing;
    }
}
