package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.R;

/**
 * Created by canturker on 05/04/15.
 */
public class HomeFragment extends Fragment {
    private Button searchButton;
    private Button nearbySearch;
    private Button myAccountSearch;
    public static final int PAGE_ID= 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment,container,false);
        searchButton = (Button)view.findViewById(R.id.searchButton);
        nearbySearch = (Button)view.findViewById(R.id.NearbySearchButton);
        myAccountSearch = (Button)view.findViewById(R.id.MyAccountButton);
        searchButton.setOnClickListener(buttonListener);
        nearbySearch.setOnClickListener(buttonListener);
        myAccountSearch.setOnClickListener(buttonListener);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
                    ((MainActivity)getActivity()).DirectFragment(new MyAccountFragment(),MainActivity.MY_ACCOUNT_POSITION);
                    break;

            }
        }
    };

}
