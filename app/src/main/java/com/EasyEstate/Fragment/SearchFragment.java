package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.SearchActivity;
import com.EasyEstate.R;

/**
 * Created by canturker on 07/05/15.
 */
public class SearchFragment extends Fragment {
    private EditText stateEditText;
    private EditText provinceEditText;
    private Button searchButton;
    private RadioGroup radioHousingTypeGroup;
    private RadioButton houseRadioButton;
    private RadioButton saleRadioButton;
    private Spinner priceSpinner;
    private static final String TAG ="SEARCH_QUERY_TAG";
    public  static final String QUERY= "QUERY";
    public static final String QUERY_DETAILS = "'QUERY_DETAILS";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_listing_fragment,container,false);
        stateEditText = (EditText)view.findViewById(R.id.stateEditText);
        provinceEditText = (EditText)view.findViewById(R.id.provinceEditText);
        searchButton = (Button)view.findViewById(R.id.searchButton);
        radioHousingTypeGroup = (RadioGroup)view.findViewById(R.id.radioHousingTypeGroup);
        houseRadioButton = (RadioButton) view.findViewById(R.id.selectHouseRadioButton);
        saleRadioButton = (RadioButton)view.findViewById(R.id.selectSaleRadioButton);
        priceSpinner = (Spinner)view.findViewById(R.id.priceSelectSpinner);
        radioHousingTypeGroup.setOnCheckedChangeListener(radioGroupListener);
        searchButton.setOnClickListener(buttonListener);
        return view;
    }
    RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId== R.id.selectSaleRadioButton){
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.priceSale, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                priceSpinner.setAdapter(adapter);
                priceSpinner.setSelection(0);
            }else{
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.priceRent, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                priceSpinner.setAdapter(adapter);
                priceSpinner.setSelection(0);
            }
        }
    };
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String queryListing = "select * from Listing,Location where Listing.AdID = Location.AdID";
            String queryListingDetails= "select * from ";
            if(houseRadioButton.isChecked()){
                queryListing += " and listingType='0'";
                queryListingDetails +=  "House";
            }else{
                queryListing += " and listingType='1'";
                queryListingDetails += "Land";
            }
            if(saleRadioButton.isChecked()){
                queryListingDetails += " where estateType='1'";
            }else{
                queryListingDetails += " where estateType='0'";
            }
            // Üşengeçlik hatası Database te Ayrı ayrı ayırmak lazım...;(
            if(provinceEditText.getText().toString().trim().length()!=0){
                queryListing += " and Location.address LIKE '%"+provinceEditText.getText().toString()+"%'";
            }
            if(stateEditText.getText().toString().trim().length()!=0){
                queryListing += " and Location.address LIKE '%"+stateEditText.getText().toString()+"%'";
            }
            if(priceSpinner.getSelectedItemPosition() !=0){
                int position = priceSpinner.getSelectedItemPosition();
                if(saleRadioButton.isChecked()){
                    switch (position){

                        case 1:
                            queryListing += " and Listing.price<50000";
                            break;
                        case 2:
                            queryListing += " and Listing.price>50000 and Listing.price>99999";
                            break;
                        case 3:
                            queryListing += " and Listing.price>100000 and Listing.price>249999";
                            break;
                        case 4:
                            queryListing += " and Listing.price>250000 and Listing.price>499999";
                            break;
                        case 5:
                            queryListing += " and Listing.price>500000 and Listing.price>749999";
                            break;
                        case 6:
                            queryListing += " and Listing.price>750000 and Listing.price>999999";
                            break;
                        case 7:
                            queryListing += " and Listing.price>1000000";
                            break;
                    }
                }else{
                    switch (position){

                        case 1:
                            queryListing += " and Listing.price<1000";
                            break;
                        case 2:
                            queryListing += " and Listing.price>1000 and Listing.price>2499";
                            break;
                        case 3:
                            queryListing += " and Listing.price>2500 and Listing.price>4999";
                            break;
                        case 4:
                            queryListing += " and Listing.price>5000 and Listing.price>7499";
                            break;
                        case 5:
                            queryListing += " and Listing.price>7500 and Listing.price>9999";
                            break;
                        case 6:
                            queryListing += " and Listing.price>10000";
                            break;
                    }
                }
            }
            Log.i(TAG,queryListing);
            Log.i(TAG,queryListingDetails);
            //Start New Activity which shows the listings according to queries.
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra(QUERY,queryListing);
            intent.putExtra(QUERY_DETAILS,queryListingDetails);
            startActivityForResult(intent, MainActivity.SEARCH_LISTING);
        }
    };
}
