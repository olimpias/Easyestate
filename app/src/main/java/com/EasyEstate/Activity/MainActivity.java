package com.EasyEstate.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.EasyEstate.Adapter.NavigationAdapter;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Fragment.HomeFragment;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;


public class MainActivity extends ActionBarActivity {
    public static final String [] MENUS = {"Home","My Account","My Favorites", "My Listings","New Listing","Edit Listing","Delete Listing"};
    private static final int [] MENU_ICON = {} ;
    private ListView drawerListView;
    public static DatabaseConnection connection = new DatabaseConnection();
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationAdapter navigationAdapter;
    private static final String EMAIL ="EMAIL";
    public static final  String SHARED_PREFERENCE_REF = "EASY_ESTATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetSavedUser();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerListView = (ListView)findViewById(R.id.left_drawer);
        navigationAdapter = new NavigationAdapter(MENUS,this,MENU_ICON);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerListView.setOnItemClickListener(new DrawerItemListener());
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame,new HomeFragment()).commit();
        setTitle(MENUS[0]);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))return true;
       /* if(item.getItemId()==R.id.action_logOut){

            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen=drawerLayout.isDrawerOpen(drawerListView);
        return drawerOpen;

    }
    /*
    user email will be taken from SharedPreference.If it is not null, it will initiate user in database connection.
     */
    private void GetSavedUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_REF,MODE_PRIVATE);
        String email = sharedPreferences.getString(EMAIL,null);
        if(email !=null){
            connection.setUser(new User(email));
        }
    }
    /*
    user will be saved on sharedPreference
     */
    private void SaveUser(String email){
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCE_REF, MODE_PRIVATE).edit();
        editor.putString(EMAIL,email);
        editor.apply();
    }
    private class DrawerItemListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selection(position);
        }
        /*
        Passing between fragments according to user choose on navigator drawer.
         */
        void selection(int position){
            Fragment fragment = null ;
            switch (position){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    break;
                default:
                    fragment =null ;
                    break;
            }
            if(connection.getUser()==null){
                if(position != 0)return;
            }
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame,fragment).commit();
            setTitle(MENUS[position]);
            drawerListView.setItemChecked(position,true);
            drawerLayout.closeDrawer(drawerListView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
