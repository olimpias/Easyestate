package com.EasyEstate.Fragment;

import android.os.AsyncTask;
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
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.ProgressLoading;

import org.json.JSONException;

import java.io.IOException;

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
    private Listing mainListing;
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
        int id;
        if(getArguments() != null)
         id = getArguments().getInt(MyListingControlActivity.AD_ID,-1);
        else
         id = -1;
        if(id != -1){
            new NetworkConnection().execute();
        }
        nextButton.setOnClickListener(buttonNextListener);
        return view;
    }
    private View.OnClickListener buttonNextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(titleEditText.getText().toString().trim().length() != 0 && descriptionEditText.getText().toString().trim().length() != 0 && squareMeterEditText.toString().trim().length() != 0 && primeEditText.toString().trim().length() != 0){
                if(mainListing == null){
                    if(houseRadioButton.isChecked()){
                    MyListingControlActivity.setListing(new House());
                     }else{
                    MyListingControlActivity.setListing(new Land());
                    }
                    mainListing = MyListingControlActivity.getListing();
                }
                mainListing.setDescription(descriptionEditText.getText().toString());
                mainListing.setTitle(titleEditText.getText().toString());
                mainListing.setSquareMeter(Integer.parseInt(squareMeterEditText.getText().toString()));
                mainListing.setPrice(Double.parseDouble(primeEditText.getText().toString()));
                if(saleRadioButton.isChecked()){
                    mainListing.setEstateType("Sale");
                }else{
                    mainListing.setEstateType("Rent");
                }
                // Change Fragment...
                if( mainListing instanceof House){
                    ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertHouseFragment());
                }else{
                    ((MyListingControlActivity)getActivity()).ChangeFragment(new InsertLandFragment());
                }
            }else{
                MainActivity.AlertDialog(getActivity(),"Fill the fields please","Warning!");
            }
        }
    };
    private class NetworkConnection extends AsyncTask<Void,Void,Listing> {
        private ProgressLoading dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressLoading(getActivity(),"Loading Data Wait...");
            dialog.show();
        }
        @Override
        protected Listing doInBackground(Void... params) {
            try {
                return DatabaseConnection.getConnection().SelectListing(MyListingControlActivity.getListing());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Listing listing) {
            super.onPostExecute(listing);
            if(dialog != null)dialog.dismiss();
            if(listing != null){
                MyListingControlActivity.setListing(listing);
                mainListing = MyListingControlActivity.getListing();
                titleEditText.setText(listing.getTitle());
                descriptionEditText.setText(listing.getDescription());
                squareMeterEditText.setText(listing.getSquareMeter()+"");
                primeEditText.setText(listing.getPrice()+"");
                if(listing.getEstateType().equals("Sale")){
                    saleRadioButton.setChecked(true);
                }else{
                    rentRadioButton.setChecked(true);
                }
                if(listing instanceof House){
                    houseRadioButton.setChecked(true);
                    landRadioButton.setChecked(false);
                }else{
                    houseRadioButton.setChecked(false);
                    landRadioButton.setChecked(true);
                }
                houseRadioButton.setEnabled(false);
                landRadioButton.setEnabled(false);
            }
        }
    }
}
