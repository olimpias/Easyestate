package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;

/**
 * Created by canturker on 16/04/15.
 */
public class InsertListingFragment extends Fragment {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText squareMeterEditText;
    private EditText primeEditText;
    private RadioButton saleRadioButton;
    private RadioButton rentRadioButton;
    private RadioButton houseRadioButton;
    private RadioButton landRadioButton;
    private SeekBar seekBar;
    private Button nextButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_fragment,container,false);
        nextButton = (Button)view.findViewById(R.id.nextButton);
        seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        titleEditText = (EditText)view.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText)view.findViewById(R.id.descriptionEditText);
        squareMeterEditText = (EditText)view.findViewById(R.id.squareMeterEditText);
        primeEditText = (EditText)view.findViewById(R.id.priceEditText);
        saleRadioButton = (RadioButton)view.findViewById(R.id.saleRadioButton);
        rentRadioButton = (RadioButton)view.findViewById(R.id.rentRadioButton);
        houseRadioButton = (RadioButton)view.findViewById(R.id.houseRadioButton);
        landRadioButton = (RadioButton)view.findViewById(R.id.landRadioButton);
        nextButton = (Button)view.findViewById(R.id.nextButton);
        seekBar.setEnabled(false);
        nextButton.setOnClickListener(buttonNextListener);
        return view;
    }
    private View.OnClickListener buttonNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(titleEditText.getText().toString().trim().length() != 0 && descriptionEditText.getText().toString().trim().length() != 0 && squareMeterEditText.toString().trim().length() != 0 && primeEditText.toString().trim().length() != 0){
                if(houseRadioButton.isChecked()){
                    MyListingControlActivity.setListing(new House());
                }else{
                    MyListingControlActivity.setListing(new Land());
                }
                Listing listing = MyListingControlActivity.getListing();
                listing.setDescription(descriptionEditText.getText().toString());
                listing.setTitle(titleEditText.getText().toString());
                listing.setSquareMeter(Integer.parseInt(squareMeterEditText.getText().toString()));
                listing.setPrice(Double.parseDouble(primeEditText.getText().toString()));
                if(saleRadioButton.isChecked()){
                    listing.setEstateType("Sale");
                }else{
                    listing.setEstateType("Rent");
                }
                // Change Fragment...
                if( listing instanceof House){
                    ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertHouseFragment());
                }else{
                    ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertLandFragment());
                }
            }else{
                MainActivity.AlertDialog(getActivity(),"Fill the fields please","Warning!");
            }
        }
    };
}
