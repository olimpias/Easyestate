package com.EasyEstate.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.ImageLoad;
import java.io.IOException;
import java.net.URL;
import java.util.List;
/**
 * Created by canturker on 29/04/15.
 */
public class ListingFavoriteAdapter extends BaseAdapter {
    private List<Listing> listingList;
    private Activity context;
    protected int serverListSize = -1;

    public List<Listing> getListingList() {
        return listingList;
    }

    public void setServerListSize(int serverListSize) {
        if(serverListSize>this.serverListSize)
            this.serverListSize = serverListSize;
    }

    // Two view types which will be used to determine whether a row should be displaying
    // data or a Progressbar
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;
    public ListingFavoriteAdapter(List<Listing> listingList,Activity context) {
        this.listingList = listingList;
        this.context=context;
    }
    private class ViewHolder{
        TextView titleTextView;
        TextView priceTextView;
        ImageView imageView;
        TextView addressTextView;
        TextView informationTextView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return listingList.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        return (position >= listingList.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ACTIVITY;
    }
    @Override
    public Object getItem(int position) {
        return getItemViewType(position) == VIEW_TYPE_ACTIVITY ? listingList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return getItemViewType(position) == VIEW_TYPE_ACTIVITY ? position : -1;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // display the last row
            return getFooterView(position, convertView, parent);
        }
        ViewHolder viewHolder = null;
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.listingrepresent_in_myfavoritelisting,null);
            viewHolder=new ViewHolder();
            viewHolder.addressTextView = (TextView)convertView.findViewById(R.id.AddressTextView);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.profilePictureImageView);
            viewHolder.informationTextView = (TextView)convertView.findViewById(R.id.informationAboutListing);
            viewHolder.priceTextView = (TextView)convertView.findViewById(R.id.priceTextView);
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.TitleTextView);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        final Listing listing = listingList.get(position);
        viewHolder.titleTextView.setText(listing.getTitle());
        viewHolder.priceTextView.setText(listing.getPrice()+"");
        viewHolder.addressTextView.setText(listing.getLocation().getAddress());
        if(listing instanceof House){

        }else{

        }
        if(listing.getImagesURL()!=null && listing.getImagesURL().size()!= 0){
            ImageLoad imageLoad = new ImageLoad(viewHolder.imageView,Listing.IMAGE_URL+listing.getImagesURL().get(0));
            new LoadImage().execute(imageLoad);
        }
        return convertView;
    }
    /**
     * returns a View to be displayed in the last row.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getFooterView(int position, View convertView,
                              ViewGroup parent) {
        if (position >= serverListSize && serverListSize > 0) {
            // the ListView has reached the last row

            TextView tvLastRow = new TextView(context);
            tvLastRow.setHint("");
            tvLastRow.setGravity(Gravity.CENTER);
            tvLastRow.setVisibility(View.VISIBLE);
            return tvLastRow;
        }

        View row = convertView;
        if (row == null) {
            row = context.getLayoutInflater().inflate(
                    R.layout.progressview, parent, false);
        }
        return row;
    }
    private class LoadImage extends AsyncTask<ImageLoad,Void,Bitmap> {
        ImageLoad imageLoad;
        @Override
        protected Bitmap doInBackground(ImageLoad... params) {
            this.imageLoad = params[0];
            try {
                return BitmapTool.newScaledThumbnailBitmap(new URL(imageLoad.getUrl()).openStream());
            } catch (IOException e) {
                e.printStackTrace();
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