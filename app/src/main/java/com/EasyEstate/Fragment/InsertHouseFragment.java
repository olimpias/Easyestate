package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.EasyEstate.R;

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
    private Button nextHouseButton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_house_fragment,container,false);
        numberOfRoomSpinner = (Spinner)view.findViewById(R.id.numberOfRoomSpinner);
        numberOfBathSpinner = (Spinner)view.findViewById(R.id.numberOfBathSpinner);
        ageOfBuildingEditText = (EditText)view.findViewById(R.id.ageOfBuildingEditText);
        numberOfFloorEditText = (EditText)view.findViewById(R.id.numberOfFloorEditText);
        currentFloorEditText = (EditText)view.findViewById(R.id.currentFloorEditText);
        duesEditText = (EditText)view.findViewById(R.id.duesEditText);
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

        }
    };
}
