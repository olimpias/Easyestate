package com.EasyEstate.Adapter;

/**
 * Created by canturker on 30/04/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.EasyEstate.Activity.ListingActivity;
import com.EasyEstate.Fragment.DetailsHouseFragment;
import com.EasyEstate.Fragment.DetailsLandFragment;
import com.EasyEstate.Fragment.OverviewFragment;


/**
 * Created by canturker on 08/04/15.
 */
public class TabPageAdapter extends FragmentPagerAdapter {
    private int id;
    private String type;
    public static final String LISTING = "LISTING";
    public TabPageAdapter(FragmentManager fm,int id, String type) {
        super(fm);
        this.id = id;
        this.type = type;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putInt(ListingActivity.AD_ID,id);
        bundle.putString(ListingActivity.AD_TYPE,type);
        switch (position){
            case 0:
                fragment = new OverviewFragment();
                fragment.setArguments(bundle);
                break;
            case 1:
                if(type.equals("0")){
                    fragment = new DetailsHouseFragment();
                }else{
                    fragment = new DetailsLandFragment();
                }
                break;

        }
        return fragment;
    }
    @Override
    public int getCount() {
        return 2;
    }
}