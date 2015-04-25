package com.EasyEstate.Database;

import android.graphics.Bitmap;
import android.util.Log;
import com.EasyEstate.Model.House;
import com.EasyEstate.Model.Land;
import com.EasyEstate.Model.Listing;
import com.EasyEstate.Model.ListingLocation;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
        if(connection == null)connection = new DatabaseConnection();
        return connection;
    }
    /*
    Convert response inputStream to String return string
     */
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
    /*
    Insert listing and images to server.If it success returns true else false
     */
    public boolean insertListing(Listing listing,Map<String,Bitmap> bitmapHashMap) throws IOException, JSONException {
        int id = -1;
        if(listing instanceof House){
            id = insertHouse(((House)listing));
        }
        else if(listing instanceof Land){
            id = insertLand(((Land)listing));
        }
        if(id != -1){
            for(String key:bitmapHashMap.keySet()){
                String filename = System.currentTimeMillis()+"_"+id+".jpeg";
                upLoadImage(id,filename,bitmapHashMap.get(key),key);
            }
        }
        if(id == -1)return false;
        return true;
    }
    /*
    Upload Image to php server
     */
    private void upLoadImage(int adID,String filename,Bitmap bitmap,String filepath) throws IOException {
        httpPost = new HttpPost(URL+"uploadImage.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        Bitmap image;
        if(filepath == null)
            image = BitmapTool.newScaledBitmap(bitmap);
        else{
            image = BitmapTool.newScaledBitmap(BitmapTool.imageOrientationValidator(bitmap,filepath));
        }
        String encodeImage = BitmapTool.encodeTo64BitBitmap(image);
        nameValuePairs.add(new BasicNameValuePair("image",encodeImage));
        nameValuePairs.add(new BasicNameValuePair("adID",adID+""));
        nameValuePairs.add(new BasicNameValuePair("filename",filename));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        Log.e(TAG,result);
    }
    /*
    Insert house values to json and sends it to php server url
    return new adID
     */
    private int insertHouse(House house) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"insertHouse.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        JSONObject jsonObject = new JSONObject();
        nameValuePairs.add(new BasicNameValuePair("email",user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("title", house.getTitle()));
        nameValuePairs.add(new BasicNameValuePair("description",house.getDescription()));
        nameValuePairs.add(new BasicNameValuePair("price",house.getPrice()+""));
        nameValuePairs.add(new BasicNameValuePair("squareMeter",house.getSquareMeter()+""));
        if(house.getEstateType().equals("Sale")){
            nameValuePairs.add(new BasicNameValuePair("estateType","1"));
        }else{
            nameValuePairs.add(new BasicNameValuePair("estateType","0"));
        }
        nameValuePairs.add(new BasicNameValuePair("numberOfRoom", house.getNumberOfRoom()+""));
        nameValuePairs.add(new BasicNameValuePair("numberOfBath", house.getNumberOfBath()+""));
        nameValuePairs.add(new BasicNameValuePair("houseAge", house.getHouseAge()+""));
        nameValuePairs.add(new BasicNameValuePair("numberOfFloor", house.getNumberOfFloor()+""));
        nameValuePairs.add(new BasicNameValuePair("currentFloor", house.getCurrentFloor()+""));
        nameValuePairs.add(new BasicNameValuePair("dues", house.getDues()+""));
        nameValuePairs.add(new BasicNameValuePair("heating", house.getHeating()+""));
        if(house.isLoanEligibilityHouse())
            nameValuePairs.add(new BasicNameValuePair("loanEligibility","1"));

        else
            nameValuePairs.add(new BasicNameValuePair("loanEligibility","0"));
        if(house.isInSideSite())
            nameValuePairs.add(new BasicNameValuePair("isInSideSite", "1"));
        else
            nameValuePairs.add(new BasicNameValuePair("isInSideSite","0"));
        nameValuePairs.add(new BasicNameValuePair("useStatus", house.getUseStatus()+""));
        nameValuePairs.add(new BasicNameValuePair("longitude",house.getLocation().getLongitude()+""));
        nameValuePairs.add(new BasicNameValuePair("latitude",house.getLocation().getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("address", house.getLocation().getAddress()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        Log.e(TAG,result);
        return new JSONObject(result).getInt("id");
    }
    /*
    Insert land values to json and sends it to php server url
    return new adID
     */
    private int insertLand(Land land) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"insertLand.php");
        JSONObject jsonObject = new JSONObject();
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();

        nameValuePairs.add(new BasicNameValuePair("email",user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("description",land.getDescription()));
        nameValuePairs.add(new BasicNameValuePair("title",land.getTitle()));
        nameValuePairs.add(new BasicNameValuePair("price",land.getPrice()+""));
        nameValuePairs.add(new BasicNameValuePair("squareMeter",land.getSquareMeter()+""));
        if(land.getEstateType().equals("Sale")){
            nameValuePairs.add(new BasicNameValuePair("estateType","1"));
        }else{
            nameValuePairs.add(new BasicNameValuePair("estateType","0"));
        }
        nameValuePairs.add(new BasicNameValuePair("zoningStatus",land.getZoningStatus()+""));
        nameValuePairs.add(new BasicNameValuePair("islandNo",land.getIslandNo()+""));
        nameValuePairs.add(new BasicNameValuePair("parcelNo",land.getParcelNo()+""));
        nameValuePairs.add(new BasicNameValuePair("layoutNo",land.getLayoutNo()+""));
        nameValuePairs.add(new BasicNameValuePair("gabari",land.getGabari()+""));
        nameValuePairs.add(new BasicNameValuePair("deedStatus",land.getDeedStatus()+""));
        if(land.isProvisionFloor())
            nameValuePairs.add(new BasicNameValuePair("provisionFloor","1"));

        else
            nameValuePairs.add(new BasicNameValuePair("provisionFloor","0"));

        if(land.isLoanEligibility())
            nameValuePairs.add(new BasicNameValuePair("loanEligibility","1"));

        else
            nameValuePairs.add(new BasicNameValuePair("loanEligibility","0"));
        nameValuePairs.add(new BasicNameValuePair("longitude",land.getLocation().getLongitude()+""));
        nameValuePairs.add(new BasicNameValuePair("latitude",land.getLocation().getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("address", land.getLocation().getAddress()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        Log.e(TAG,result);
        return new JSONObject(result).getInt("id");
    }


    public boolean deleteListing(Listing listing) throws IOException, JSONException{
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("adID", listing.getAdID() + ""));
        if(listing instanceof House)
            nameValuePairs.add(new BasicNameValuePair("listingType", "0"));
        else
            nameValuePairs.add(new BasicNameValuePair("listingType", "1"));
        nameValuePairs.add(new BasicNameValuePair("adID", listing.getAdID()+""));
        httpPost = new HttpPost(URL+"deleteListing.php");
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        Log.e(TAG,result);
        return (new JSONObject(result).getInt("Code") == 1) ? true : false;
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
        nameValuePairs.add(new BasicNameValuePair("name",user.getName()));
        nameValuePairs.add(new BasicNameValuePair("image", user.getImageURL()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.getInt("Code") == 1){
            return true;
        }
        return false;
    }

    public boolean UpdateUser(User user) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"updateUser.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",user.getEmail()));
        nameValuePairs.add(new BasicNameValuePair("phone",user.getPhone()));
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
    /*
    Returns the total number of login user inserted listing.

     */
    public int OwnerTotalListingCount () throws UserDoesNotLoginException, IOException, JSONException {
        httpPost = new HttpPost(URL+"countOfTotalOwnerListing.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",getUser().getEmail()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getInt("count");
    }
    /*
    return List of Listings.User is owner of them.
     */
    public List<Listing> getMyListings(int offset) throws UserDoesNotLoginException, IOException, JSONException {
        httpPost = new HttpPost(URL+"selectOwnerListings.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("email",getUser().getEmail()));
        nameValuePairs.add(new BasicNameValuePair("offset", offset + ""));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONArray jsonArray = new JSONArray(result);
        List<Listing> listingList = new ArrayList<>();
        for(int i = 0;i<jsonArray.length();i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            Listing listing;
            if(jsonObject.getString("listingType").equals("0")){
                listing = new House();
            }
            else{
                listing = new Land();
            }
            listing.setLocation(new ListingLocation(jsonObject.getDouble("longitute"),jsonObject.getDouble("latitude"),jsonObject.getString("address")));
            listing.setAdID(jsonObject.getInt("adID"));
            listing.setTitle(jsonObject.getString("title"));
            listing.setDescription(jsonObject.getString("description"));
            listing.setPrice(jsonObject.getDouble("price"));
            JSONArray images = jsonObject.getJSONArray("images");
            listing.setImagesURL(new ArrayList<String>());
            for(int j = 0;j<images.length();j++){
                JSONObject jsonObject1 = (JSONObject)images.get(j);
                listing.getImagesURL().add(jsonObject1.getString("imageURL"));
            }
            listingList.add(listing);
        }
        return listingList;
    }/*
    *Return Listing, that all attributes are filled.
    *
    * */
    public Listing SelectListing(Listing listing) throws IOException, JSONException {
        httpPost = new HttpPost(URL+"selectListing.php");
        ArrayList<NameValuePair> nameValuePairs = InitializingKey();
        nameValuePairs.add(new BasicNameValuePair("adID",listing.getAdID()+""));
        if (listing instanceof House){
            nameValuePairs.add(new BasicNameValuePair("listingType","0"));
        }else{
            nameValuePairs.add(new BasicNameValuePair("listingType","1"));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpClient.execute(httpPost);
        String result = getResponse(response);
        JSONObject jsonObject = new JSONObject(result);
        JSONObject basicListJson = jsonObject.getJSONObject("list");
        if(basicListJson.getString("listingType").equals("0")){
            listing =new House();
        }else{
            listing = new Land();
        }
        listing.setAdID(basicListJson.getInt("adID"));
        listing.setLocation(new ListingLocation(basicListJson.getDouble("longitute"), basicListJson.getDouble("latitude"), basicListJson.getString("address")));
        listing.setTitle(basicListJson.getString("title"));
        listing.setDescription(basicListJson.getString("description"));
        listing.setPrice(basicListJson.getDouble("price"));
        JSONArray images = jsonObject.getJSONArray("images");
        listing.setImagesURL(new ArrayList<String>());
        for(int j = 0;j<images.length();j++){
            JSONObject jsonObject1 = (JSONObject)images.get(j);
            listing.getImagesURL().add(jsonObject1.getString("imageURL"));
        }
        JSONObject jsonObject2 = jsonObject.getJSONObject("listing");
        if(listing instanceof House){
            House house = (House)listing;
            house.setHouseAge(jsonObject2.getInt("houseAge"));
            house.setHeating(jsonObject2.getInt("houseAge"));
            house.setNumberOfRoom(jsonObject2.getInt("numberOfRoom"));
            house.setNumberOfBath(jsonObject2.getInt("numberOfBath"));
            house.setNumberOfFloor(jsonObject2.getInt("numberOfFloor"));
            house.setCurrentFloor(jsonObject2.getInt("currentFloor"));
            house.setDues(jsonObject2.getInt("dues"));
            house.setSquareMeter(jsonObject2.getInt("squareMeter"));
            house.setUseStatus(jsonObject2.getInt("useStatus"));
            if(jsonObject2.getString("isInSideSite").equals("1")){
                house.setInSideSite(true);
            }else{
                house.setInSideSite(false);
            }
            if(jsonObject2.getString("estateType").equals("1")){
                house.setEstateType("Sale");
            }else{
                house.setEstateType("Rent");
            }
            if(jsonObject2.getString("loanEligibilityHouse").equals("1")){
                house.setLoanEligibilityHouse(true);
            }else{
                house.setLoanEligibilityHouse(false);
            }

        }else{
            Land land = (Land)listing;
            land.setParcelNo(jsonObject2.getInt("parcelNo"));
            land.setGabari(jsonObject2.getInt("gabari"));
            land.setIslandNo(jsonObject2.getInt("islandNo"));
            land.setZoningStatus(jsonObject2.getInt("zoningStatus"));
            land.setDeedStatus(jsonObject2.getInt("deedStatus"));
            land.setSquareMeter(jsonObject2.getInt("squareMeter"));
            land.setLayoutNo(jsonObject2.getInt("layoutNo"));
            if(jsonObject2.getString("loanEligibility").equals("1")){
                land.setLoanEligibility(true);
            }else{
                land.setLoanEligibility(false);
            }
            if(jsonObject2.getString("estateType").equals("1")){
                land.setEstateType("Sale");
            }else{
                land.setEstateType("Rent");
            }
            if(jsonObject2.getString("provisionFloor").equals("1")){
                land.setProvisionFloor(true);
            }else{
                land.setProvisionFloor(false);
            }
        }
        return listing;
    }

}

