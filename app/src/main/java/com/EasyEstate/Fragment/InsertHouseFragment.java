package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Model.House;
import com.EasyEstate.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by canturker on 16/04/15.
 */
public class InsertHouseFragment extends Fragment {
    private Spinner numberOfRoomSpinner;
    private Spinner numberOfBathSpinner;
    private EditText ageOfBuildingEditText;
    private EditText numberOfFloorEditText;
    private EditText currentFloorEditText;
    private EditText duesEditText;
    private Spinner heatingSpinner;
    private CheckBox loanEligibilityHouseCheckBox;
    private Spinner useStatusSpinner;
    private CheckBox inTheSiteCheckBox;
    private SeekBar seekBar;
    private Button nextHouseButton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_house_fragment,container,false);
        numberOfRoomSpinner = (Spinner)view.findViewById(R.id.numberOfRoomSpinner);
        numberOfBathSpinner = (Spinner)view.findViewById(R.id.numberOfBathSpinner);
        ageOfBuildingEditText = (EditText)view.findViewById(R.id.ageOfBuildingEditText);
        numberOfFloorEditText = (EditText)view.findViewById(R.id.numberOfFloorEditText);
        currentFloorEditText = (EditText)view.findViewById(R.id.currentFloorEditText);
        duesEditText = (EditText)view.findViewById(R.id.duesEditText);
        seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        heatingSpinner = (Spinner)view.findViewById(R.id.heatingSpinner);
        loanEligibilityHouseCheckBox = (CheckBox)view.findViewById(R.id.LoanEligibilityHouseCheckBox);
        useStatusSpinner = (Spinner)view.findViewById(R.id.useStatusSpinner);
        inTheSiteCheckBox = (CheckBox)view.findViewById(R.id.InTheSiteCheckBox);
        nextHouseButton = (Button)view.findViewById(R.id.nextHouseButton);
        nextHouseButton.setOnClickListener(nextButtonListener);
        return view;
    }
    private View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(numberOfRoomSpinner.getSelectedItemPosition() != 0 && numberOfBathSpinner.getSelectedItemPosition()!= 0&&useStatusSpinner.getSelectedItemPosition()!=0&& heatingSpinner.getSelectedItemPosition()!= 0&&
                    ageOfBuildingEditText.getText().toString().trim().length() != 0 && numberOfFloorEditText.getText().toString().trim().length() != 0
                    && currentFloorEditText.getText().toString().trim().length() != 0 && duesEditText.getText().toString().trim().length() != 0 ){
                House house =(House)MyListingControlActivity.getListing();
                house.setCurrentFloor(currentFloorEditText.getText().toString());
                house.setNumberOfFloor(numberOfFloorEditText.getText().toString());
                house.setNumberOfBath(numberOfBathSpinner.getSelectedItemPosition());
                house.setNumberOfRoom(numberOfRoomSpinner.getSelectedItemPosition());
                house.setHeating(heatingSpinner.getSelectedItemPosition());
                house.setInSideSite(inTheSiteCheckBox.isChecked());
                house.setLoanEligibilityHouse(loanEligibilityHouseCheckBox.isChecked());
                house.setUseStatusSpinner(useStatusSpinner.getSelectedItemPosition());
                ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertListingMapFragment());
            }else{
                MainActivity.AlertDialog(getActivity(), "Fill the fields please", "Warning!");

            }
        }
    };
}
