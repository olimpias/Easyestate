package com.EasyEstate.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.EasyEstate.Adapter.TitleNavigationAdapter;
import com.EasyEstate.Fragment.ListingSearchFragment;
import com.EasyEstate.Fragment.MapSearchFragment;
import com.EasyEstate.Fragment.SearchFragment;
import com.EasyEstate.R;
import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity implements ActionBar.OnNavigationListener{
private String query = null;
private String queryDetails = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listing_control);
        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("ListView");
        itemList.add("MapView");
        query = getIntent().getStringExtra(SearchFragment.QUERY);
        queryDetails = getIntent().getStringExtra(SearchFragment.QUERY_DETAILS);
        if(query == null || queryDetails == null)finish();
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        TitleNavigationAdapter arrayAdapter = new TitleNavigationAdapter(this,itemList);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(arrayAdapter, this);
        changeFragment(0);
    }
    private void changeFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString(SearchFragment.QUERY,query);
        bundle.putString(SearchFragment.QUERY_DETAILS,queryDetails);
        if(position == 1){
            fragment = new MapSearchFragment();
        }else{
            fragment = new ListingSearchFragment();
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        changeFragment(i);
        return false;
    }
}
