package com.EasyEstate.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.EasyEstate.Adapter.TabPageAdapter;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Database.UserDoesNotLoginException;
import com.EasyEstate.Fragment.DetailsHouseFragment;
import com.EasyEstate.Fragment.DetailsLandFragment;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.Communicator;

import org.json.JSONException;

import java.io.IOException;

public class ListingActivity extends ActionBarActivity implements ActionBar.TabListener,Communicator {
    private ViewPager viewPager;
    private TabPageAdapter pageAdapter;
    public static final String AD_ID = "AD_ID";
    private static final String TAG = "LISTING_ACTIVITY";
    private int id;
    private int favoriteFlag;
    private static final int STAR_ON = android.R.drawable.star_big_on;
    private static final int STAR_OFF = android.R.drawable.star_big_off;
    public static final String AD_TYPE = "ADD_TYPE";
    private static final int LISTING = 10;
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
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        getMenuInflater().inflate(R.menu.menu_listingaddfavorite, menu);
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
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        if(id == R.id.FavoriteAdd){
            //Start Intent if user is not login.
            new NetworkFavoriteOperation().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.FavoriteAdd);
        if(favoriteFlag == 0){
            menuItem.setIcon(STAR_OFF);
        }else{
            menuItem.setIcon(STAR_ON);
        }
        return super.onPrepareOptionsMenu(menu);
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
    public void Respond(Listing listing,int favoriteFlag) {
        this.id = listing.getAdID();
      if(listing instanceof House){
          DetailsHouseFragment detailsHouseFragment = (DetailsHouseFragment)getSupportFragmentManager().getFragments().get(1);
          detailsHouseFragment.ListingDataChanged(listing);
      }else{
          DetailsLandFragment detailsLandFragment = (DetailsLandFragment)getSupportFragmentManager().getFragments().get(1);
          detailsLandFragment.ListingDataChanged(listing);
      }
        Log.i(TAG,favoriteFlag+"");
        this.favoriteFlag = favoriteFlag;
        invalidateOptionsMenu();

    }
    private class NetworkFavoriteOperation extends AsyncTask<Void,Void,Boolean>{
        byte flag ;
        @Override
        protected Boolean doInBackground(Void... params) {

                try {
                    if(favoriteFlag == 1) {
                        return DatabaseConnection.getConnection().deleteFavoriteAdd(id);
                    }else{
                        return DatabaseConnection.getConnection().insertFavoriteAdd(id);
                    }
                } catch (UserDoesNotLoginException e) {
                    e.printStackTrace();
                    flag = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean != null && aBoolean){
                favoriteFlag = (favoriteFlag+1)%2;
                invalidateOptionsMenu();
                String message="";
                if(favoriteFlag == 0){
                    message = "Unsaved";
                }else{
                    message = "Saved";
                }
                Toast.makeText(ListingActivity.this,message,Toast.LENGTH_SHORT).show();
            }else{
                if(aBoolean == null && flag == 1){
                    Intent intent = new Intent(ListingActivity.this,LoginActivity.class);
                    startActivityForResult(intent,LISTING);
                }
            }
        }
    }
}
