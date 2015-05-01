package com.EasyEstate.Adapter;

/**
 * Created by canturker on 01/05/15.
 */

import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


import android.app.Activity;
import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.EasyEstate.Fragment.OverviewFragment;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;


/**
 * Created by canturker on 27/03/15.
 */
public class ImageSliderAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<String> imageList;
    OverviewFragment homeFragment;

    public ImageSliderAdapter(Activity activity,ArrayList<String> imageList,
                             OverviewFragment homeFragment) {
        this.activity = activity;
        this.homeFragment = homeFragment;
        this.imageList = imageList;

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vp_image, container, false);

        ImageView mImageView = (ImageView) view
                .findViewById(R.id.image_display);

        ImageLoad imageLoad = new ImageLoad(Listing.IMAGE_URL+imageList.get(position),mImageView);
        new LoadImageFromURL().execute(imageLoad);
        container.addView(view);
        return view;
    }
    private class ImageLoad{
        String urlLink;
        ImageView image;
        ImageLoad(String urlLink,ImageView image){
            this.urlLink = urlLink;
            this.image = image;
        }
    }
    private class LoadImageFromURL extends AsyncTask<ImageLoad,Void,Bitmap> {

        ImageLoad imageLoad;
        @Override
        protected Bitmap doInBackground(ImageLoad... params) {
            imageLoad = params[0];
            if (imageLoad !=null)
                try {
                    return BitmapTool.newScaledBitmap(new URL(imageLoad.urlLink).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                imageLoad.image.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}