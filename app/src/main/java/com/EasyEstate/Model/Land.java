package com.EasyEstate.Model;

/**
 * Created by canturker on 04/04/15.
 */
public class Land extends  Listing {
    private int zoningStatus;
    private int islandNo;
    private int parcelNo;
    private int layoutNo;
    private int gabari;
    private int deedStatus;
    private boolean provisionFloor;
    private boolean loanEligibility;


    public Land(String adID) {
        super(adID);
    }
    public Land(){}

    public boolean isLoanEligibility() {
        return loanEligibility;
    }

    public void setLoanEligibility(boolean loanEligibility) {
        this.loanEligibility = loanEligibility;
    }

    public void setDeedStatus(int deedStatus) {
        this.deedStatus = deedStatus;
    }

    public int getDeedStatus() {
        return deedStatus;
    }

    public int getZoningStatus() {
        return zoningStatus;
    }

    public void setZoningStatus(int zoningStatus) {
        this.zoningStatus = zoningStatus;
    }

    public int getIslandNo() {
        return islandNo;
    }

    public void setIslandNo(int islandNo) {
        this.islandNo = islandNo;
    }

    public int getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(int parcelNo) {
        this.parcelNo = parcelNo;
    }

    public int getLayoutNo() {
        return layoutNo;
    }

    public void setLayoutNo(int layoutNo) {
        this.layoutNo = layoutNo;
    }

    public int getGabari() {
        return gabari;
    }

    public void setGabari(int gabari) {
        this.gabari = gabari;
    }



    public boolean isProvisionFloor() {
        return provisionFloor;
    }

    public void setProvisionFloor(boolean provisionFloor) {
        this.provisionFloor = provisionFloor;
    }
}
