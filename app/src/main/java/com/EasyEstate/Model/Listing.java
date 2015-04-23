package com.EasyEstate.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by canturker on 04/04/15.
 *
 */
//!! ListingType in database 1 represent Land, 0 represent House
public abstract class Listing {
    private  int adID;
    private ArrayList<String> imagesURL;
    private String description;
    private String title;
    private Double price;
    private int squareMeter;
    private String estateType;
    private ListingLocation location;
    private User listingOwner;

    protected Listing(int adID){
        this.setAdID(adID);
    }
    protected Listing(){}

    public int getAdID() {
        return adID;
    }

    public void setAdID(int adID) {
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

    public User getListingOwner() {
        return listingOwner;
    }

    public void setListingOwner(User listingOwner) {
        this.listingOwner = listingOwner;
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
