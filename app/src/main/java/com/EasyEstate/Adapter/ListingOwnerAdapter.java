package com.EasyEstate.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.ImageLoad;
import com.EasyEstate.SupportTool.ProgressLoading;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * Created by canturker on 07/03/15.
 */
public class ListingOwnerAdapter extends BaseAdapter {
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
    public ListingOwnerAdapter(List<Listing> listingList,Activity context) {
        this.listingList = listingList;
        this.context=context;
    }
    private class ViewHolder{
        TextView titleTextView;
        TextView adIDTextView;
        ImageView imageView;
        ImageButton editListingButton;
        ImageButton deleteListingButton;
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
        ViewHolder viewHolder;
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.listingrepresent_in_mylistingfragment,null);
            viewHolder=new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.TitleTextView);
            viewHolder.adIDTextView = (TextView)convertView.findViewById(R.id.IdTextView);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.PictureImageView);
            viewHolder.deleteListingButton = (ImageButton)convertView.findViewById(R.id.DeleteImageButton);
            viewHolder.editListingButton = (ImageButton)convertView.findViewById(R.id.EditImageButton);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        final Listing listing = listingList.get(position);
        viewHolder.titleTextView.setText(listing.getTitle());
        viewHolder.adIDTextView.setText("ID: "+listing.getAdID());
        viewHolder.deleteListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarningDialog(listing);
            }
        });
        viewHolder.deleteListingButton.setFocusable(false);
        viewHolder.deleteListingButton.setFocusableInTouchMode(false);
        viewHolder.editListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start new Activity
                Intent intent = new Intent(context, MyListingControlActivity.class);
                intent.putExtra(MyListingControlActivity.AD_ID,listing.getAdID());
                if(listing instanceof House)
                    intent.putExtra(MyListingControlActivity.LISTING_TYPE,0);
                else
                    intent.putExtra(MyListingControlActivity.LISTING_TYPE,1);
                MainActivity.PAGE = MainActivity.INSERT_LISTING;
                context.startActivityForResult(intent,MainActivity.INSERT_LISTING);
            }
        });
        viewHolder.editListingButton.setFocusable(false);
        viewHolder.editListingButton.setFocusableInTouchMode(false);
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
    private void WarningDialog(final Listing listing){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new NetworkConnection().execute(listing);
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private class NetworkConnection extends AsyncTask<Listing,Void,Boolean>{
        private ProgressLoading progressLoading;
        private Listing listing;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading = new ProgressLoading(context,null);
            progressLoading.show();
        }

        @Override
        protected Boolean doInBackground(Listing... params) {
            try {
                listing = params[0];
                return DatabaseConnection.getConnection().deleteListing(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progressLoading != null)progressLoading.dismiss();
            if(aBoolean){
                MainActivity.AlertDialog(context,"Deleted Successfully","Information");
                listingList.remove(listing);
                serverListSize --;
                notifyDataSetChanged();
            }else{
                MainActivity.AlertDialog(context,"Error Occurred","Error");
            }
        }
    }
}