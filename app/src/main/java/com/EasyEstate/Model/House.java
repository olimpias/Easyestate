package com.EasyEstate.Model;

import java.util.ArrayList;

/**
 * Created by canturker on 04/04/15.
 */
public class House extends  Listing {
    private String numberOfRoom;
    private String numberOfBath;
    private String houseAge;
    private String numberOfFloor;
    private String currentFloor;
    private int aidat;
    private String heating;
    private String category;
    private boolean esyali;
    private boolean siteIcerisinde;
    private String kullanimDurumu;
    public House(String adID) {
        super(adID);
    }
    public House(){}
    public House(String adID, ArrayList<String> imagesURL, String description, String title, Double price, int squareMeter, String estateType, boolean post, boolean krediyeUygun, ListingLocation location, User listingOwner) {
        super(adID, imagesURL, description, title, price, squareMeter, estateType, post, krediyeUygun, location, listingOwner);
    }

    public String getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(String numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public String getNumberOfBath() {
        return numberOfBath;
    }

    public void setNumberOfBath(String numberOfBath) {
        this.numberOfBath = numberOfBath;
    }

    public String getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(String houseAge) {
        this.houseAge = houseAge;
    }

    public String getNumberOfFloor() {
        return numberOfFloor;
    }

    public void setNumberOfFloor(String numberOfFloor) {
        this.numberOfFloor = numberOfFloor;
    }

    public int getAidat() {
        return aidat;
    }

    public void setAidat(int aidat) {
        this.aidat = aidat;
    }

    public String getHeating() {
        return heating;
    }

    public void setHeating(String heating) {
        this.heating = heating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEsyali() {
        return esyali;
    }

    public void setEsyali(boolean esyali) {
        this.esyali = esyali;
    }

    public boolean isSiteIcerisinde() {
        return siteIcerisinde;
    }

    public void setSiteIcerisinde(boolean siteIcerisinde) {
        this.siteIcerisinde = siteIcerisinde;
    }

    public String getKullanimDurumu() {
        return kullanimDurumu;
    }

    public void setKullanimDurumu(String kullanimDurumu) {
        this.kullanimDurumu = kullanimDurumu;
    }

    public String getCurrentFloor() { return currentFloor; }

    public void setCurrentFloor(String currentFloor) { this.currentFloor = currentFloor;}
}
