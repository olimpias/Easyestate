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

        }
    };
}
