package com.EasyEstate.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.EasyEstate.Adapter.TabPageAdapter;
import com.EasyEstate.Fragment.DetailsHouseFragment;
import com.EasyEstate.Fragment.DetailsLandFragment;
import com.EasyEstate.Fragment.MapFragment;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.Communicator;

import java.util.List;

public class ListingActivity extends ActionBarActivity implements ActionBar.TabListener,Communicator {
    private ViewPager viewPager;
    private TabPageAdapter pageAdapter;
    public static final String AD_ID = "AD_ID";
    public static final String AD_TYPE = "ADD_TYPE";
    private static final String [] tabs = {"OVERVIEW","DETAILS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houselandviewpager);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        viewPager  = (ViewPager)findViewById(R.id.pager);
        int id = getIntent().getIntExtra(AD_ID,-1);
        String type = getIntent().getStringExtra(AD_TYPE);
        if(id == -1)finish();
        pageAdapter = new TabPageAdapter(getSupportFragmentManager(),id,type);
        viewPager.setAdapter(pageAdapter);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for(int i = 0 ; i < tabs.length ;i ++){
            actionBar.addTab(actionBar.newTab().setText(tabs[i]).setTabListener(this));
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,null);
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing, menu);
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
        if(id == R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void Respond(Listing listing) {
      if(listing instanceof House){
          DetailsHouseFragment detailsHouseFragment = (DetailsHouseFragment)getSupportFragmentManager().getFragments().get(1);
      }else{
          DetailsLandFragment detailsLandFragment = (DetailsLandFragment)getSupportFragmentManager().getFragments().get(1);
      }
    }
}
