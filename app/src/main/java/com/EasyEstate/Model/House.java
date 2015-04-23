package com.EasyEstate.Model;

import java.util.ArrayList;

public class House extends  Listing {
    private int numberOfRoom;
    private int numberOfBath;
    private int houseAge;
    private int numberOfFloor;
    private int currentFloor;
    private int dues;
    private int heating;
    private boolean loanEligibilityHouse;
    private boolean isInSideSite;
    private int useStatus;
    public House(int adID) {
        super(adID);
    }
    public House(){}

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
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

    public int getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(int houseAge) {
        this.houseAge = houseAge;
    }

    public int getNumberOfFloor() {
        return numberOfFloor;
    }

    public void setNumberOfFloor(int numberOfFloor) {
        this.numberOfFloor = numberOfFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}
