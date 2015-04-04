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

    public Land(String adID, boolean katKarsiligi, String tapuDurumu, int gabari, int pafraNo, int parseNo, int adaNo, String imarDurumu) {
        super(adID);
        this.katKarsiligi = katKarsiligi;
        this.tapuDurumu = tapuDurumu;
        this.gabari = gabari;
        this.pafraNo = pafraNo;
        this.parseNo = parseNo;
        this.adaNo = adaNo;
        this.imarDurumu = imarDurumu;
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
