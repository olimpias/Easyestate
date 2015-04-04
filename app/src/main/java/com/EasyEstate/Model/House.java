package com.EasyEstate.Model;

/**
 * Created by canturker on 04/04/15.
 */
public class House extends  Listing {
    private int numberOfRoom;
    private int numberOfBath;
    private int houseAge;
    private int numberOfFloor;

    protected House(String adID) {
        super(adID);
    }
}
