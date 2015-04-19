package com.EasyEstate.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.EasyEstate.Fragment.InsertListingFragment;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;

public class MyListingControlActivity extends ActionBarActivity {
 private static Listing listing;
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
            listing = null;

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InsertListingFragment()).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Listing getListing() {
        return listing;
    }

    public static void setListing(Listing listing) {
        MyListingControlActivity.listing = listing;
    }
}
