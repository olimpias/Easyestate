package com.EasyEstate.Database;

import com.EasyEstate.Model.User;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by canturker on 04/04/15.
 */
public class DatabaseConnection {
    private User user;
    private static final String URL = "www.yeicmobil.com";
    private static final String secretKey = "1234ASD";
    private HttpClient httpClient ;
    private HttpPost httpPost ;

    public DatabaseConnection() {
        httpClient = new DefaultHttpClient();

    }
    
}
