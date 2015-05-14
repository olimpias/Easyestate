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

import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;

/**
 * Created by canturker on 30/04/15.
 */
public class DetailsHouseFragment extends Fragment {
    private Listing listing;
    private TextView descriptionTextView;
    private TextView sizeTextView;
    private TextView estateTypeTextView;
    private TextView roomTextView;
    private TextView bathTextView;
    private TextView ageBuildingTextView;
    private TextView numberOfFloorTextView;
    private TextView currentFloorTextView;
    private TextView heatingTextView;
    private TextView duesTextView;
    private TextView loanEligibilityTextView;
    private TextView isInsideSiteTextView;
    private TextView useStatusTextView;
    private String [] heatingTypes;
    private String [] roomTypes;
    private String [] bathTypes;
    private String [] useStatusTypes;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_house_fragment, container, false);
        descriptionTextView = (TextView)view.findViewById(R.id.descriptionTextView);
        sizeTextView = (TextView)view.findViewById(R.id.sizeTextView);
        estateTypeTextView = (TextView)view.findViewById(R.id.estateTypeTextView);
        roomTextView = (TextView)view.findViewById(R.id.roomTextView);
        bathTextView = (TextView)view.findViewById(R.id.bathTextView);
        ageBuildingTextView = (TextView)view.findViewById(R.id.ageBuildingTextView);
        numberOfFloorTextView = (TextView)view.findViewById(R.id.numberOfFloorTextView);
        currentFloorTextView = (TextView)view.findViewById(R.id.currentFloorTextView);
        heatingTextView = (TextView)view.findViewById(R.id.heatingTextView);
        duesTextView = (TextView)view.findViewById(R.id.duesTextView);
        loanEligibilityTextView = (TextView)view.findViewById(R.id.loanEligibilityTextView);
        isInsideSiteTextView = (TextView)view.findViewById(R.id.isInsideSiteTextView);
        useStatusTextView = (TextView)view.findViewById(R.id.useStatusTextView);
        heatingTypes = getResources().getStringArray(R.array.planets_array);
        roomTypes = getResources().getStringArray(R.array.planets_array);
        bathTypes = getResources().getStringArray(R.array.planets_array);
        useStatusTypes = getResources().getStringArray(R.array.planets_array);
        return view;
    }
    public void ListingDataChanged(Listing listing){
        setListing(listing);
        //Add Values to views.
        House house = (House)listing;
        descriptionTextView.setText(listing.getDescription());
        sizeTextView.setText(listing.getSquareMeter()+"m");
        ageBuildingTextView.setText(house.getHouseAge()+"");
        currentFloorTextView.setText(house.getCurrentFloor()+"");
        numberOfFloorTextView.setText(house.getNumberOfFloor()+"");
        roomTextView.setText(roomTypes[house.getNumberOfRoom()]);
        bathTextView.setText(bathTypes[house.getNumberOfBath()]);
        useStatusTextView.setText(useStatusTypes[house.getUseStatus()]);
        heatingTextView.setText(heatingTypes[house.getHeating()]);
        estateTypeTextView.setText(house.getEstateType());
        duesTextView.setText(house.getDues()+"TL");
        if(house.isInSideSite()) {
            isInsideSiteTextView.setText("Yes");
        }else{
            isInsideSiteTextView.setText("No");
        }
        if(house.isLoanEligibilityHouse()){
            loanEligibilityTextView.setText("Yes");
        }else{
            loanEligibilityTextView.setText("No");
        }
    }
    private void setListing(Listing listing) {
        this.listing = listing;
    }
}
