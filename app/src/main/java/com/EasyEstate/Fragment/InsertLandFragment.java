package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.EasyEstate.R;

/**
 * Created by canturker on 16/04/15.
 */
public class InsertLandFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_land_fragment,container,false);

        return view;
    }
}
