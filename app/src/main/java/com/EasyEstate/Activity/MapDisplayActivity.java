package com.EasyEstate.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.EasyEstate.Fragment.MapFragment;
import com.EasyEstate.R;

public class MapDisplayActivity extends ActionBarActivity {
public  static final String LONGITUDE = "LONGITUDE";
public  static final String ADDRESS = "ADDRESS";
public static final String LATITUDE = "LATITUDE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dislay);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (findViewById(R.id.fragment_container) != null) {
            double longitute = getIntent().getDoubleExtra(LONGITUDE,0);
            double latitude = getIntent().getDoubleExtra(LATITUDE,0);
            String address = getIntent().getStringExtra(ADDRESS);
            Fragment fragment = new MapFragment();
            Bundle bundle = new Bundle();
            bundle.putDouble(LONGITUDE,longitute);
            bundle.putDouble(LATITUDE,latitude);
            bundle.putString(ADDRESS,address);
            fragment.setArguments(bundle);
            ChangeFragment(fragment);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,null);
        finish();
        super.onBackPressed();

    }

    public  void ChangeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_dislay, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
