package com.EasyEstate.Model;

/**
 * Created by canturker on 04/04/15.
 */
public class ListingLocation {
    private double longitude;
    private double latitude;
    private String province;
    private String district;
    private String country;

    public ListingLocation(double longitude,double latitude,String province,String district,String country){
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setProvince(province);
        this.setDistrict(district);
        this.setCountry(country);
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    //View da belirtmek için sonradan eklenicek
    @Override
    public String toString() {
        return "";
    }
}
