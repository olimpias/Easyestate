package com.EasyEstate.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_land_fragment,container,false);
        nextButton = (Button)view.findViewById(R.id.nextButton);
        zoningStatusSpinner = (Spinner)view.findViewById(R.id.ZoningStatusSpinner);
        islandNoEditText = (EditText)view.findViewById(R.id.islandNoEditText);
        parcelNoEditText = (EditText)view.findViewById(R.id.parcelNoEditText);
        layoutNoEditText = (EditText)view.findViewById(R.id.layoutNoEditText);
        gabariSpinner = (Spinner)view.findViewById(R.id.GabariSpinner);
        deedStatusSpinner = (Spinner)view.findViewById(R.id.deedStatusSpinner);
        provisionFloorCheckBox = (CheckBox)view.findViewById(R.id.provisionFloorCheckBox);
        loanEligibilityCheckBox = (CheckBox)view.findViewById(R.id.LoanEligibilityLandCheckBox);
        nextButton.setOnClickListener(nextButtonListener);
        return view;
    }
    private View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
