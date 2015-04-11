package com.EasyEstate.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.EasyEstate.Activity.MainActivity;
import com.EasyEstate.Activity.ProfileActivity;
import com.EasyEstate.Database.UserDoesNotLoginException;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by canturker on 09/04/15.
 */
public class MyAccountFragment extends Fragment {
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private ImageView profilePictureImageView;
    public static final int PAGE_ID= 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myaccountfragment,container,false);
        phoneTextView = (TextView)view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView)view.findViewById(R.id.emailTextView);
        nameTextView = (TextView)view.findViewById(R.id.nameTextView);
        profilePictureImageView = (ImageView)view.findViewById(R.id.profilePictureImageView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            new NetworkConnection().execute(MainActivity.connection.getUser().getEmail());
        } catch (UserDoesNotLoginException e) {
            e.printStackTrace();

        }
    }
    private class NetworkConnection extends AsyncTask<String,Void,User>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... params) {
            try {
                MainActivity.connection.SelectUser(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                return  MainActivity.connection.getUser();
            } catch (UserDoesNotLoginException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if(user != null){
                if(user.getPhone() != null )
                phoneTextView.setText(user.getPhone());
                if(user.getName() != null)
                nameTextView.setText(user.getName());

                emailTextView.setText(user.getEmail());
                if(user.getImageURL() != null && user.getImageURL().length()!=0)
                new LoadImage().execute(user.getLoadImageURL());
            }
        }
    }
    private class LoadImage extends  AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return BitmapTool.getRoundedShape(BitmapTool.newScaledThumbnailBitmap(new URL(params[0]).openStream()));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                profilePictureImageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_myaccount,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.OpenProfileEdit:
                Intent intent = new Intent(getActivity(),ProfileActivity.class);
                MainActivity.PAGE = PAGE_ID;
                startActivityForResult(intent,MainActivity.PROFILE_EDIT);
                break;
        }
        return true;
    }
}
