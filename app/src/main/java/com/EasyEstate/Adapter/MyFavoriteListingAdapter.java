package com.EasyEstate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.ImageLoad;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by canturker on 15/04/15.
 */
public class MyFavoriteListingAdapter extends BaseAdapter{
    private Context context;
    List<Listing> listingList;
    public MyFavoriteListingAdapter(Context context){
        this.context = context;
        listingList = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listingList.size();
    }

    @Override
    public Object getItem(int position) {
        return listingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        ImageView pictureImageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView addressTextView;
        ImageButton favoriteButton;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            holder.titleTextView = (TextView)convertView.findViewById(R.id.TitleTextView);
            holder.addressTextView = (TextView)convertView.findViewById(R.id.AddressTextView);
            holder.favoriteButton = (ImageButton)convertView.findViewById(R.id.FavoriteImageButton);
            holder.pictureImageView = (ImageView)convertView.findViewById(R.id.PictureImageView);
            holder.priceTextView = (TextView)convertView.findViewById(R.id.priceTextView);
            convertView.setTag(convertView);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Listing listing = listingList.get(position);
        holder.titleTextView.setText(listing.getTitle());
        holder.addressTextView.setText(listing.getLocation().getAddress());//Edit here!!!!!
        holder.priceTextView.setText(listing.getPrice()+"");

        if(listing.getImagesURL().size()!=0){
            ImageLoad imageLoad = new ImageLoad(holder.pictureImageView,listing.getImagesURL().get(0));
            new LoadImage().execute(imageLoad);
        }
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove From Favorite
            }
        });
        return convertView;
    }
    private class LoadImage extends AsyncTask<ImageLoad,Void,Bitmap> {
        ImageLoad imageLoad;
        @Override
        protected Bitmap doInBackground(ImageLoad... params) {
            this.imageLoad = params[0];
            try {
                return BitmapTool.newScaledThumbnailBitmap(new URL(imageLoad.getUrl()).openStream());
            } catch (IOException e) {
                return null;
            }

        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                imageLoad.getImageView().setImageBitmap(bitmap);
            }
        }
    }
}
