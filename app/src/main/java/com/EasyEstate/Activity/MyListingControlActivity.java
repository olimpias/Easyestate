package com.EasyEstate.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Fragment.InsertListingFragment;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.ProgressLoading;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MyListingControlActivity extends ActionBarActivity {
 private static Listing listing;
 private static boolean isEditing;
 public static final String AD_ID = "adID";
 public static final String LISTING_TYPE = "listingType";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listing_control);
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            int id = getIntent().getExtras().getInt(AD_ID,-1);
            int listingType= getIntent().getExtras().getInt(LISTING_TYPE,-1);
            if(id == -1){
                listing = null;
                isEditing = false;
            }
            else{
                if(listingType == 0){

                    listing = new House();
                }else{
                    listing = new Land();
                }
                isEditing = true;
                listing.setAdID(id);
                try {
                   MyListingControlActivity.listing = new NetworkConnection().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            // Add the fragment to the 'fragment_container' FrameLayout
        ChangeFragment(new InsertListingFragment());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public static boolean isEditing() {
        return isEditing;
    }
    public static Listing getListing() {
        return listing;
    }

    public static void setListing(Listing listing) {
        MyListingControlActivity.listing = listing;
    }
    public  void ChangeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        WarningDialog();
    }
    private void WarningDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure to close it? You will loose all data.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
        private class NetworkConnection extends AsyncTask<Void,Void,Listing>{
        private ProgressLoading dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressLoading(MyListingControlActivity.this,"Loading Data Wait...");
            dialog.show();
        }
        @Override
        protected Listing doInBackground(Void... params) {
            try {
                return DatabaseConnection.getConnection().SelectListing(MyListingControlActivity.listing);
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
        }
    }
}
