package com.EasyEstate.Activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.EasyEstate.Database.UserDoesNotLoginException;
import com.EasyEstate.Fragment.HomeFragment;
import com.EasyEstate.Fragment.MyAccountFragment;
import com.EasyEstate.Fragment.MyFavoritesFragment;
import com.EasyEstate.Fragment.MyListingsFragment;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;


public class MainActivity extends ActionBarActivity {
    public static final String [] MENUS = {"Home","My Account","My Favorites", "My Listings"};
    private static final int [] MENU_ICON = {} ;
    private ListView drawerListView;
    public static DatabaseConnection connection = new DatabaseConnection();
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private static final String TAG = "MainActivity";
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationAdapter navigationAdapter;
    public static final int HOME_POSITION = 0;
    public static final int MY_ACCOUNT_POSITION = 1;
    public static final int MY_FAVORITES_POSITION = 2;
    public static final int MY_LISTING_POSITION = 3;
    protected static final String EMAIL ="EMAIL";
    protected static final int LOGIN_FLAG = 4;
    protected static final String IS_IT_FIRST_LOGIN="FIRSTLOGIN";
    public  static final int INSERT_LISTING = 3;
    public static final int PROFILE_EDIT =1;
    protected static final  String SHARED_PREFERENCE_REF = "EASY_ESTATE";
    public static int PAGE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetSavedUser();
        /*
        //Session Blocker...
        connection.setUser(null);
        */
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
        drawerListView.setAdapter(navigationAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemListener());
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame,new HomeFragment()).commit();
        setTitle(MENUS[0]);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))return true;
        if(item.getItemId() == R.id.Logout){
            deleteUser();
            connection.setUser(null);
            ChangeFragment(new HomeFragment(),0);
            invalidateOptionsMenu();
        }
       /* if(item.getItemId()==R.id.action_logOut){

            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    private void deleteUser(){
        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.SHARED_PREFERENCE_REF, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
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

    private class DrawerItemListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selection(position);
        }
        /*
        Passing between fragments according to user choose on navigator drawer.
         */

    }
    private void LogOut(){

    }
    void selection(int position){
        Fragment fragment = null ;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new MyAccountFragment();
                break;
            case 2:
                fragment = new MyFavoritesFragment();
                break;
            case 3:
                fragment = new MyListingsFragment();
                break;
            default:
                fragment =null ;
                break;
        }

        DirectFragment(fragment,position);
    }
    private void ChangeFragment(Fragment fragment,int position){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment).commit();
        setTitle(MENUS[position]);
        drawerListView.setItemChecked(position,true);
        drawerLayout.closeDrawer(drawerListView);
    }
    public void DirectFragment(Fragment fragment,int position){
        try {
            if(connection.getUser()!= null){
                ChangeFragment(fragment,position);
            }
        } catch (UserDoesNotLoginException e) {
            e.printStackTrace();
            if(position == 0){
                ChangeFragment(fragment,position);
            }else{
                PAGE = position;
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,LOGIN_FLAG);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == LOGIN_FLAG){
                this.invalidateOptionsMenu();
                boolean isFirst = data.getBooleanExtra(IS_IT_FIRST_LOGIN,false);
                if(isFirst) {
                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivityForResult(intent,MainActivity.PROFILE_EDIT);
                }else{
                    if(PAGE != -1){
                        selection(PAGE);
                        PAGE = -1;
                    }else{
                        selection(0);
                    }
                }

            }
            if(requestCode == PROFILE_EDIT){
                if(PAGE != -1){
                    selection(PAGE);
                    PAGE = -1;
                }else{
                    selection(0);
                }
            }
            if(requestCode == INSERT_LISTING){
                if(PAGE != -1){
                    selection(PAGE);
                    PAGE = -1;
                }else{
                    selection(0);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            if(connection.getUser() != null){
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        } catch (UserDoesNotLoginException e) {
            e.printStackTrace();
            getMenuInflater().inflate(R.menu.menu_default_main, menu);

        }
        return true;
    }

    public static void AlertDialog (Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ERROR");
        builder.setMessage(message);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}