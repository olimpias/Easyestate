package com.EasyEstate.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by canturker on 04/04/15.
 */
public abstract class Listing {
    private  String adID;
    private ArrayList<String> imagesURL;
    private String description;
    private String title;
    private Double price;
    private int squareMeter;
    private String  estateType;
    private ListingLocation location;
    private User listingOwner;

    protected Listing(String adID){
        this.setAdID(adID);
    }
    protected Listing(){}

    protected Listing(String adID,ArrayList<String> imagesURL,String description,String title,Double price,int squareMeter,String estateType,ListingLocation location,User listingOwner){
        this.adID=adID;
        this.imagesURL = imagesURL;
        this.description = description;
        this.title = title;
        this.price = price;
        this.squareMeter = squareMeter;
        this.estateType = estateType ;
        this.location = location;
        this.listingOwner = listingOwner;
    }
    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public ListingLocation getLocation() {
        return location;
    }

    public void setLocation(ListingLocation location) {
        this.location = location;
    }

    public ArrayList<String> getImagesURL() {
        return imagesURL;
    }



    public void setImagesURL(ArrayList<String> imagesURL) {
        this.imagesURL = imagesURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getSquareMeter() {
        return squareMeter;
    }

    public void setSquareMeter(int squareMeter) {
        this.squareMeter = squareMeter;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }

}
