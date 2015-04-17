package com.EasyEstate.Model;

import java.util.ArrayList;

/**
 * Created by canturker on 04/04/15.
 */
public class Land extends  Listing {
    private String imarDurumu;
    private int adaNo;
    private int parseNo;
    private int pafraNo;
    private int gabari;
    private String tapuDurumu;
    private boolean katKarsiligi;


    public Land(String adID) {
        super(adID);
    }
    public Land(){}
    public Land(String adID, ArrayList<String> imagesURL, String description, String title, Double price, int squareMeter, String estateType, boolean post, boolean krediyeUygun, ListingLocation location, User listingOwner) {
        super(adID, imagesURL, description, title, price, squareMeter, estateType, post, krediyeUygun, location, listingOwner);
    }

    public String getImarDurumu() {
        return imarDurumu;
    }

    public void setImarDurumu(String imarDurumu) {
        this.imarDurumu = imarDurumu;
    }

    public int getAdaNo() {
        return adaNo;
    }

    public void setAdaNo(int adaNo) {
        this.adaNo = adaNo;
    }

    public int getParseNo() {
        return parseNo;
    }

    public void setParseNo(int parseNo) {
        this.parseNo = parseNo;
    }

    public int getPafraNo() {
        return pafraNo;
    }

    public void setPafraNo(int pafraNo) {
        this.pafraNo = pafraNo;
    }

    public int getGabari() {
        return gabari;
    }

    public void setGabari(int gabari) {
        this.gabari = gabari;
    }

    public String getTapuDurumu() {
        return tapuDurumu;
    }

    public void setTapuDurumu(String tapuDurumu) {
        this.tapuDurumu = tapuDurumu;
    }

    public boolean isKatKarsiligi() {
        return katKarsiligi;
    }

    public void setKatKarsiligi(boolean katKarsiligi) {
        this.katKarsiligi = katKarsiligi;
    }
}
