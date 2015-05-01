package com.EasyEstate.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.EasyEstate.Activity.ListingActivity;
import com.EasyEstate.Activity.MapDisplayActivity;
import com.EasyEstate.Adapter.CirclePageIndicator;
import com.EasyEstate.Adapter.ImageSliderAdapter;
import com.EasyEstate.Adapter.PageIndicator;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.Communicator;
import com.EasyEstate.SupportTool.ProgressLoading;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by canturker on 30/04/15.
 */
public class OverviewFragment extends Fragment {
    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;
    private ViewPager mViewPager;
    private PageIndicator mIndicator;
    private TextView descriptionTextView;
    private TextView adIDTextView;
    private TextView priceTextView;
    private TextView estateTypeTextView;
    private TextView nameContactTextView;
    private TextView emailContactTextView;
    private TextView phoneContactTextView;
    private ImageView contactImageView;
    private Button mapDisplayButton;
    private Listing list;
    private Handler handler;
    private Runnable animateViewPager;
    private boolean stopSliding = false;
    private Communicator communicator;
    private Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overview_fragment, container, false);
        final int id = getArguments().getInt(ListingActivity.AD_ID,-1);
        String type = getArguments().getString(ListingActivity.AD_TYPE);
        if(type.equals("0")){
            list = new House();
        }else{
            list = new Land();
        }
        list.setAdID(id);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        descriptionTextView = (TextView)view.findViewById(R.id.descriptionTextView);
        adIDTextView = (TextView)view.findViewById(R.id.adIDTextView);
        priceTextView = (TextView)view.findViewById(R.id.priceTextView);
        mapDisplayButton = (Button)view.findViewById(R.id.mapDisplayButton);
        estateTypeTextView = (TextView)view.findViewById(R.id.estateTypeTextView);
        nameContactTextView = (TextView)view.findViewById(R.id.contactNameTextView);
        emailContactTextView = (TextView)view.findViewById(R.id.contactEmailTextView);
        phoneContactTextView = (TextView)view.findViewById(R.id.contactPhoneTextView);
        contactImageView = (ImageView)view.findViewById(R.id.contactImageView);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (list != null && list.getImagesURL().size() != 0) {
                            stopSliding = false;

                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                        }
                        break;
                }
                return false;
            }
        });

            mapDisplayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
                    intent.putExtra(MapDisplayActivity.ADDRESS, list.getLocation().getAddress());
                    intent.putExtra(MapDisplayActivity.LATITUDE, list.getLocation().getLatitude());
                    intent.putExtra(MapDisplayActivity.LONGITUDE, list.getLocation().getLongitude());
                    getActivity().startActivityForResult(intent, Activity.RESULT_OK);
                }
            });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(list.getListingOwner()== null){
            new NetworkConnection().execute(list);
        }else{
            mViewPager.setAdapter( new ImageSliderAdapter(activity,list.getImagesURL(),OverviewFragment.this));
            mIndicator.setViewPager(mViewPager);
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }
    @Override
    public void onPause() {
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
        super.onPause();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class NetworkConnection extends AsyncTask<Listing,Void,Listing>{
        private ProgressLoading progressLoading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading = new ProgressLoading(getActivity(),"Uploading Listing.Please Wait...");
            progressLoading.show();
        }

        @Override
        protected Listing doInBackground(Listing... params) {
            try {
                return DatabaseConnection.getConnection().SelectListing(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Listing listing) {
            super.onPostExecute(listing);
            if(listing!=null){
                list = listing;
                communicator.Respond(listing);
                descriptionTextView.setText(listing.getDescription());
                adIDTextView.setText(listing.getAdID()+"");
                priceTextView.setText(listing.getPrice()+"");
                estateTypeTextView.setText(listing.getEstateType());
                nameContactTextView.setText(listing.getListingOwner().getName());
                emailContactTextView.setText(listing.getListingOwner().getEmail());
                phoneContactTextView.setText(listing.getListingOwner().getPhone());
                new LoadImage().execute(listing.getListingOwner().getImageURL());
                mViewPager.setAdapter(new ImageSliderAdapter(
                        activity, listing.getImagesURL(), OverviewFragment.this));

                mIndicator.setViewPager(mViewPager);
            }
            if(progressLoading != null) progressLoading.dismiss();


        }
    }
    private class LoadImage extends  AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return BitmapTool.getRoundedShape(BitmapTool.URLConnection(new URL(params[0]).openStream()));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                contactImageView.setImageBitmap(bitmap);
            }
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }
}
