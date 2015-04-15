package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.EasyEstate.R;

/**
 * Created by canturker on 12/04/15.
 */
public class MyListingsFragment extends Fragment {

    private ListView listingListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listinglistfragment,container,false);
        listingListView = (ListView)view.findViewById(R.id.ListingListView);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mylistingaddmenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.AddListingButton:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
