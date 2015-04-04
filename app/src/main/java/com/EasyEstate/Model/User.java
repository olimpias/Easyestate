package com.EasyEstate.Model;

import java.util.ArrayList;

/**
 * Created by canturker on 04/04/15.
 */
public class User {
    private String email;
    private String phone;
    private String imageURL;
    private ArrayList<Listing> userListings;
    private ArrayList<Listing> userFavorites;
    private String name;

    public User(String email) {
        this.setEmail(email);
        setUserListings(new ArrayList<Listing>());
        setUserFavorites(new ArrayList<Listing>());
    }
    public void addUserListing(Listing newListing){
        userListings.add(newListing);
    }
    public void addUserFavorites(Listing addFavorite){
        userFavorites.add(addFavorite);
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<Listing> getUserListings() {
        return userListings;
    }

    public void setUserListings(ArrayList<Listing> userListings) {
        this.userListings = userListings;
    }

    public ArrayList<Listing> getUserFavorites() {
        return userFavorites;
    }

    public void setUserFavorites(ArrayList<Listing> userFavorites) {
        this.userFavorites = userFavorites;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
