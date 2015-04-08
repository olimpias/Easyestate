package com.EasyEstate.Database;

import android.media.session.MediaSessionManager;

import com.EasyEstate.Model.House;
import com.EasyEstate.Model.User;


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
    private static final String URL = "www.yeicmobil.com/EasyEstate/";
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
        return result;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() {
        return user;
    }

    public boolean uploadHouse(House house){
        String result = null;
        try {
            httpPost=new HttpPost("http://yeicmobil.com");
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


}

