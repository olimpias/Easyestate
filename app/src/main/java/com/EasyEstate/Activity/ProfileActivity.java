package com.EasyEstate.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.EasyEstate.Database.DatabaseConnection;
import com.EasyEstate.Database.UserDoesNotLoginException;
import com.EasyEstate.Model.User;
import com.EasyEstate.R;
import com.EasyEstate.SupportTool.BitmapTool;
import com.EasyEstate.SupportTool.ProgressLoading;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class ProfileActivity extends ActionBarActivity {
    private TextView nameEditText;
    private EditText phoneEditText;
    private TextView emailTextView;
    private ImageView profileImage;
    private Button doneButton;
    private User user;
    private final static int RESULT_LOAD_IMG = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameEditText = (TextView)findViewById(R.id.nameEditView);
        phoneEditText = (EditText)findViewById(R.id.phoneEditView);
        emailTextView = (TextView)findViewById(R.id.emailTextView);
        profileImage = (ImageView)findViewById(R.id.profilePictureImageView);
        doneButton = (Button)findViewById(R.id.doneButton);
        doneButton.setOnClickListener(buttonClickListener);
        try {
            user =  DatabaseConnection.getConnection().getUser();
        } catch (UserDoesNotLoginException e) {
            e.printStackTrace();
        }
        nameEditText.setText(user.getName());
        phoneEditText.setText(user.getPhone());
        emailTextView.setText(user.getEmail());
        if(user.getImageURL().length()!=0){
            new LoadImage().execute(user.getImageURL());
        }
    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId() == doneButton.getId()){
                user.setPhone(phoneEditText.getText().toString());
                user.setName(nameEditText.getText().toString());
                new NetworkConnection().execute(user);
            }
        }
    };



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMG && resultCode == RESULT_OK && data !=null ){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();
            isChanged = true;
            // Set the Image in ImageView
            this.profilPic = BitmapTool.getRoundedShape(BitmapTool.imageOrientationValidator(BitmapTool.generateBit(imgPath),imgPath));
            profileImage.setImageBitmap(profilPic);
        }


    }
    */
    private class NetworkConnection extends AsyncTask<User,Void,Boolean>{
        ProgressLoading progressLoading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading = new ProgressLoading(ProfileActivity.this,null);
            progressLoading.show();
        }

        @Override
        protected Boolean doInBackground(User... params) {
            try {

                return  DatabaseConnection.getConnection().UpdateUser(params[0]);
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
            if(progressLoading != null){
                progressLoading.dismiss();
            }
            if(aBoolean){
                finish();
            }else{
                MainActivity.AlertDialog(ProfileActivity.this,"Connection problem",null);
            }
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
                profileImage.setImageBitmap(bitmap);
            }
        }
    }
    // Add Image
}
