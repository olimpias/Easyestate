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
    private boolean post;
    private boolean krediyeUygun;

    protected Listing(String adID){
        this.setAdID(adID);
    }

    protected Listing(String adID,ArrayList<String> imagesURL,String description,String title,Double price,int squareMeter,String estateType,boolean post, boolean krediyeUygun){
        this.adID=adID;
        this.imagesURL = imagesURL;
        this.description = description;
        this.title = title;
        this.price = price;
        this.squareMeter = squareMeter;
        this.estateType = estateType ;
        this.post = post;
        this.krediyeUygun = krediyeUygun;
    }
    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public ArrayList<String> getImagesURL() {
        return imagesURL;
    }

    public boolean isKrediyeUygun() {
        return krediyeUygun;
    }

    public void setKrediyeUygun(boolean krediyeUygun) {
        this.krediyeUygun = krediyeUygun;
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

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }
}
