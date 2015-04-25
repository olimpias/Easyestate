package com.EasyEstate.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.MyListingControlActivity;
import com.EasyEstate.Adapter.PictureChooseAdapter;
import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.ProgressLoading;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by canturker on 17/04/15.
 */
public class InsertImageFragment extends Fragment {
    private ListView listView;
    private Button nextButton;
    private Button galleryButton;
    private Button takePictureButton;
    private static int RESULT_LOAD_IMG = 1;
    private static final int CAPTURE_PHOTO=0;
    private static final int RESULT_PHOTO_CHOOSE =1;
    private SeekBar seekbar;
    private Listing listing;
    private LruCache<String,Bitmap> mMemoryCache;
    private PictureChooseAdapter adaptor;
    private InsertImageFragment insertImageFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_listing_image_fragment,container,false);
        listView = (ListView)view.findViewById(R.id.photoChooseListView);
        nextButton = (Button)view.findViewById(R.id.nextImageButton);
        galleryButton = (Button)view.findViewById(R.id.galleryButton);
        takePictureButton = (Button)view.findViewById(R.id.takePictureButton);
        seekbar = (SeekBar)view.findViewById(R.id.seekBar2);
        seekbar.setEnabled(false);
        galleryButton.setOnClickListener(buttonListeners);
        takePictureButton.setOnClickListener(buttonListeners);
        nextButton.setOnClickListener(buttonListeners);
        insertImageFragment = this;
        listing = MyListingControlActivity.getListing();

        adaptor=new PictureChooseAdapter(getActivity(),insertImageFragment);
        listView.setAdapter(adaptor);
        if(MyListingControlActivity.isEditing()){
            new GetImages().execute();
        }
        return view;
    }
    private class GetImages extends AsyncTask<Void,Void,Void>{
        ProgressLoading progressLoading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading = new ProgressLoading(getActivity(),"Upload images please wait");
            progressLoading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(listing.getImagesURL()!=null){
                for(int i = 0;i<listing.getImagesURL().size();i++){
                    try {
                        adaptor.AddImage(BitmapTool.newScaledBitmap(new URL(Listing.IMAGE_URL+listing.getImagesURL().get(i)).openStream()),i+"");

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressLoading != null) progressLoading.dismiss();
            adaptor.notifyDataSetChanged();
        }
    }
    private View.OnClickListener buttonListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == nextButton.getId()){
                //Begin Database Insert Operation...
                new NetworkConnection().execute();
            }
            if(v.getId() == galleryButton.getId()){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
            if(v.getId() == takePictureButton.getId()){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAPTURE_PHOTO);

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount()/1024;
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && data !=null ){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();
            // Set the Image in ImageView

            adaptor.AddImage(BitmapTool.generateBit(imgPath),imgPath);
            adaptor.notifyDataSetChanged();
        }
        if(requestCode == CAPTURE_PHOTO  && data !=null ){
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            bp = BitmapTool.newScaledBitmap(bp);
            String name = System.currentTimeMillis()+".jpeg";
            adaptor.AddImage(bp,name);
            adaptor.notifyDataSetChanged();
        }
    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    private class NetworkConnection extends AsyncTask<Void,Void,Boolean>{
    private ProgressLoading dialog ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressLoading(getActivity(),"Please wait uploading...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return DatabaseConnection.getConnection().insertListing(listing,mMemoryCache.snapshot());
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
            if(dialog != null)dialog.dismiss();
            if(aBoolean){
                InformationDialog();
            }else{
                MainActivity.AlertDialog(getActivity(),"Error occurred","Error");
            }
        }
    }
    private void InformationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information");
        builder.setMessage("Listing is saved successfully.");
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    public void remove(String name){
        mMemoryCache.remove(name);
    }
}
