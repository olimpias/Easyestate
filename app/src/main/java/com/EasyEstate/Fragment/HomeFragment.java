package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.EasyEstate.R;

/**
 * Created by canturker on 05/04/15.
 */
public class HomeFragment extends Fragment {
    private Button searchButton;
    private Button nearbySearch;
    private Button myAccountSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment,container,false);
        searchButton = (Button)container.findViewById(R.id.searchButton);
        nearbySearch = (Button)container.findViewById(R.id.NearbySearchButton);
        myAccountSearch = (Button)container.findViewById(R.id.MyAccountButton);
        searchButton.setOnClickListener(buttonListener);
        nearbySearch.setOnClickListener(buttonListener);
        myAccountSearch.setOnClickListener(buttonListener);
        return view;
    }
    /*
    Buttons will replace home fragment with selected.
     */
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.search_button:

                    break;

                case R.id.NearbySearchButton :

                    break;
                case R.id.MyAccountButton:

                    break;

            }
        }
    };
}
