package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Model.Land;
import com.EasyEstate.R;

/**
 * Created by canturker on 16/04/15.
 */
public class InsertLandFragment extends Fragment {
    private Button nextButton;
    private Spinner zoningStatusSpinner;
    private EditText islandNoEditText;
    private EditText parcelNoEditText;
    private EditText layoutNoEditText;
    private Spinner gabariSpinner;
    private Spinner deedStatusSpinner;
    private CheckBox provisionFloorCheckBox;
    private CheckBox loanEligibilityCheckBox;
    private SeekBar seekbar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_land_fragment,container,false);
        nextButton = (Button)view.findViewById(R.id.nextLandButton);
        zoningStatusSpinner = (Spinner)view.findViewById(R.id.ZoningStatusSpinner);
        islandNoEditText = (EditText)view.findViewById(R.id.islandNoEditText);
        parcelNoEditText = (EditText)view.findViewById(R.id.parcelNoEditText);
        layoutNoEditText = (EditText)view.findViewById(R.id.layoutNoEditText);
        gabariSpinner = (Spinner)view.findViewById(R.id.GabariSpinner);
        deedStatusSpinner = (Spinner)view.findViewById(R.id.deedStatusSpinner);
        provisionFloorCheckBox = (CheckBox)view.findViewById(R.id.provisionFloorCheckBox);
        loanEligibilityCheckBox = (CheckBox)view.findViewById(R.id.LoanEligibilityLandCheckBox);
        seekbar = (SeekBar)view.findViewById(R.id.seekBar);
        seekbar.setEnabled(false);
        nextButton.setOnClickListener(nextButtonListener);
        return view;
    }
    private View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(zoningStatusSpinner.getSelectedItemPosition() != 0 &&islandNoEditText.getText().toString().trim().length() != 0 &&
                    parcelNoEditText.getText().toString().trim().length() != 0 && layoutNoEditText.getText().toString().trim().length() != 0 &&
                    gabariSpinner.getSelectedItemPosition()!= 0&& deedStatusSpinner.getSelectedItemPosition()!=0
                    ){
                Land land = (Land) MyListingControlActivity.getListing();
                land.setGabari(gabariSpinner.getSelectedItemPosition());
                land.setDeedStatus(deedStatusSpinner.getSelectedItemPosition());
                land.setZoningStatus(zoningStatusSpinner.getSelectedItemPosition());
                land.setProvisionFloor(provisionFloorCheckBox.isChecked());
                land.setLoanEligibility(loanEligibilityCheckBox.isChecked());
                land.setIslandNo(Integer.parseInt(islandNoEditText.getText().toString()));
                land.setParcelNo(Integer.parseInt(parcelNoEditText.getText().toString()));
                land.setLayoutNo(Integer.parseInt(layoutNoEditText.getText().toString()));
                ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertListingMapFragment());
            }else{
                MainActivity.AlertDialog(getActivity(), "Fill the fields please", "Warning!");

            }
        }
    };
}
