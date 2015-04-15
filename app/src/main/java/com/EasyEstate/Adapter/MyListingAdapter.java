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
public class MyListingAdapter extends BaseAdapter {
    private Context context;
    List<Listing> listingList;
    public MyListingAdapter(Context context){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listingrepresent_in_mylistingfragment,parent,false);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.TitleTextView);
            holder.idTextView = (TextView)convertView.findViewById(R.id.IdTextView);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.DeleteImageButton);
            holder.editButton = (ImageButton) convertView.findViewById(R.id.EditImageButton);
            holder.pictureImageView = (ImageView)convertView.findViewById(R.id.PictureImageView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Listing listing = listingList.get(position);

        holder.titleTextView.setText(listing.getTitle());
        holder.idTextView.setText(listing.getAdID());
        //ButtonListeners....
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start New Activity...
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete with database connection after that alert dialog for commit or show error.
                // Then delete value from this adapter and user listings.
            }
        });
        //UploadImage from url.... Check if ImageURl size is not 0.Upload first URL...
        if(listing.getImagesURL().size()!=0){
            ImageLoad imageLoad = new ImageLoad(holder.pictureImageView,listing.getImagesURL().get(0));
            new LoadImage().execute(imageLoad);
        }
        return convertView;
    }
    private class ViewHolder {
        ImageView pictureImageView;
        TextView titleTextView;
        TextView idTextView;
        ImageButton deleteButton;
        ImageButton editButton;
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
