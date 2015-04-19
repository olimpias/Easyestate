package com.EasyEstate.Model;

import java.util.ArrayList;

/**
 * Created by canturker on 04/04/15.
 */
public class House extends  Listing {
    private int numberOfRoom;
    private int numberOfBath;
    private String houseAge;
    private String numberOfFloor;
    private String currentFloor;
    private int dues;
    private int heating;
    private int category;
    private boolean loanEligibilityHouse;
    private boolean isInSideSite;
    private int useStatusSpinner;
    public House(String adID) {
        super(adID);
    }
    public House(){}

    public void setUseStatusSpinner(int useStatusSpinner) {
        this.useStatusSpinner = useStatusSpinner;
    }

    public int getUseStatusSpinner() {
        return useStatusSpinner;
    }

    public int getNumberOfBath() {
        return numberOfBath;
    }

    public void setNumberOfBath(int numberOfBath) {
        this.numberOfBath = numberOfBath;
    }

    public int getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(int numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public void setHeating(int heating) {
        this.heating = heating;
    }

    public int getHeating() {
        return heating;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
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

    public int getDues() {
        return dues;
    }

    public void setDues(int dues) {
        this.dues = dues;
    }

    public boolean isLoanEligibilityHouse() {
        return loanEligibilityHouse;
    }

    public void setLoanEligibilityHouse(boolean loanEligibilityHouse) {
        this.loanEligibilityHouse = loanEligibilityHouse;
    }

    public boolean isInSideSite() {
        return isInSideSite;
    }

    public void setInSideSite(boolean inSideSite) {
        this.isInSideSite = inSideSite;
    }



    public String getCurrentFloor() { return currentFloor; }

    public void setCurrentFloor(String currentFloor) { this.currentFloor = currentFloor;}
}
