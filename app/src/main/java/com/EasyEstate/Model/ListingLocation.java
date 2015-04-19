package com.EasyEstate.Model;

/**
 * Created by canturker on 04/04/15.
 */
public class ListingLocation {
    private double longitude;
    private double latitude;
    private String address;
    public ListingLocation(){}
    public ListingLocation(double longitude,double latitude,String Address){
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setAddress(Address);

    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    //View da belirtmek için sonradan eklenicek
}
