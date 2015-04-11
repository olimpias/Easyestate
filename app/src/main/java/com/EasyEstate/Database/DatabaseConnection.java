package com.EasyEstate.Database;

import android.graphics.Bitmap;
import android.media.session.MediaSessionManager;
import android.util.Log;

import com.EasyEstate.Model.House;
import com.EasyEstate.Model.User;
import com.EasyEstate.SupportTool.BitmapTool;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class DatabaseConnection {
    private User user;
    private static final String TAG ="DATABASE_CONNECTON";
    private static final String URL = "http://www.yeicmobil.com/EasyEstate/";
    private static final String secretKey = "1234ASD";
    private HttpClient httpClient ;
    private HttpGet httpGet;
    private HttpPost httpPost ;

    public DatabaseConnection() {
        httpClient = new DefaultHttpClient();

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

    public boolean uploadHouse(House house){
        String result = null;
        try {
            httpPost=new HttpPost(URL);
            JSONObject json=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            try {
                json.put("ilanBasligi", house.getTitle());
                json.put("adID", house.getAdID());
                json.put("description", house.getDescription());
                json.put("price", house.getPrice());
                json.put("bathNum", house.getNumberOfBath());
                json.put("roomNum", house.getNumberOfRoom());
                json.put("m2", house.getSquareMeter());
                json.put("binaYasi", house.getHouseAge());
                json.put("katSayisi", house.getNumberOfFloor());
                json.put("bulunduguKat", house.getCurrentFloor());
                json.put("aidat", house.getAidat());
                json.put("isitma", house.getHeating());
                json.put("esyali", house.isEsyali());
                json.put("kullanimDurumu", house.getKullanimDurumu());
                json.put("siteIcerisinde", house.isSiteIcerisinde());
                json.put("krediyeUygunluk", house.isKrediyeUygun());
                json.put("post", house.isPost());
                json.put("ilanTipi", house.getEstateType());
                json.put("category", house.getCategory());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            httpPost.setHeader("json", json.toString());
            StringEntity se=new StringEntity(json.toString());
            se.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);


            try {
                HttpResponse response= httpClient.execute(httpPost);
                result=getResponse(response);


            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject object= null;
            try {
                object = new JSONObject(result);
                if(object.getInt("code")==1){
                    return true;

                }
                else{
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return false;
    }
    private ArrayList<NameValuePair> InitializingKey(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("secretKey",secretKey));
        return nameValuePairs;
    }
    public boolean isUserExit(String email) throws IOException, JSONException {
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
    public boolean InsertUser(User user) throws IOException, JSONException {
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

