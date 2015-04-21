package com.EasyEstate.Database;

import android.graphics.Bitmap;
import android.util.Log;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.Model.User;
import com.EasyEstate.SupportTool.BitmapTool;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;


public class DatabaseConnection {
    private User user;
    private static final String TAG ="DATABASE_CONNECTON";
    private static final String URL = "http://www.yeicmobil.com/EasyEstate/";
    private static final String secretKey = "1234ASD";
    private HttpClient httpClient ;
    private HttpGet httpGet;
    private HttpPost httpPost ;
    private static DatabaseConnection connection;
    private DatabaseConnection() {
        httpClient = new DefaultHttpClient();

    }

    public static DatabaseConnection getConnection() {
        if(connection != null)connection = new DatabaseConnection();
        return connection;
    }

    private String getResponse(HttpResponse response) throws IOException {
        InputStream is =response.getEntity().getContent();
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(is,"iso-8859-1"),8);
        StringBuilder sb = new StringBuilder();
        String line=null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        is.close();
        String result = sb.toString();
        Log.d(TAG,result);
        return result;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() throws UserDoesNotLoginException {
        if(user == null){
            throw new UserDoesNotLoginException();
        }else{
            return user;
        }
    }
    public boolean insertListing(Listing listing,Map<String,Bitmap> bitmapHashMap){

        return false;
    }

    private int insertHouse(House house){
        return -1;
    }
    private int insertLand(Land land){
        return -1;
    }
    private ArrayList<NameValuePair> InitializingKey(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("secretKey",secretKey));
        return nameValuePairs;
    }
    /*
       it uses in LoginUser Method checks if the email already exits in database
     */
    private boolean isUserExit(String email) throws IOException, JSONException {
         httpPost = new HttpPost(URL+"isUserExits.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",email));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.getInt("Code") == 1){
            return true;
        }
        return false;
    }
    /*
    It logs in the user.Return value represents that if user is logging in first time or not
    Return value true represents first time.False represent second or more time..
     */
    public boolean LoginUser (User user) throws IOException, JSONException {

         if(!isUserExit(user.getEmail())){
            boolean result = InsertUser(user);
            if (result)this.user = user;
            return true;
        }else{
            this.user = user;
            return false;
        }

    }
    /*
    Insert new User to database
     */
    private boolean InsertUser(User user) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"insertUser.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",user.getEmail()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.getInt("Code") == 1){
            return true;
        }
        return false;
    }
    public boolean UpdateUser(User user,Bitmap bitmap) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"updateUser.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("name",user.getName()));
        nameValuePairs.add(new BasicNameValuePair("phone",user.getPhone()));
        if(bitmap != null)
        nameValuePairs.add(new BasicNameValuePair("image", BitmapTool.encodeTo64BitBitmap(bitmap)));
        else
        nameValuePairs.add(new BasicNameValuePair("image", "NULL"));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.getInt("code") == 1){
            return true;
        }
        return false;
    }
    public boolean SelectUser(String email) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"selectUser.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",email));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        if(!jsonObject.isNull("imageURL"))
        this.user.setImageURL(jsonObject.getString("imageURL"));
        if(!jsonObject.isNull("name"))
        this.user.setName(jsonObject.getString("name"));
        if(!jsonObject.isNull("phone"))
        this.user.setPhone(jsonObject.getString("phone"));
        return true;
    }

}

