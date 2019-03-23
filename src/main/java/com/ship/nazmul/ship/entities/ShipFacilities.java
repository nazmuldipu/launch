package com.ship.nazmul.ship.entities;

import javax.persistence.Embeddable;

@Embeddable
public class ShipFacilities {
    private boolean casino;
    private boolean shops;
    private boolean spa;
    private boolean fitnessCenter;
    private boolean library;
    private boolean theatre;
    private boolean cinema;
    private boolean swimmingPool;
    private boolean hotTub;
    private boolean restaurant;
    private boolean lounges;
    private boolean gym;
    private boolean bar;
    private boolean wifi;
    private boolean kidsPlayRoom;

    public boolean isCasino() {
        return casino;
    }

    public void setCasino(boolean casino) {
        this.casino = casino;
    }

    public boolean isShops() {
        return shops;
    }

    public void setShops(boolean shops) {
        this.shops = shops;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isFitnessCenter() {
        return fitnessCenter;
    }

    public void setFitnessCenter(boolean fitnessCenter) {
        this.fitnessCenter = fitnessCenter;
    }

    public boolean isLibrary() {
        return library;
    }

    public void setLibrary(boolean library) {
        this.library = library;
    }

    public boolean isTheatre() {
        return theatre;
    }

    public void setTheatre(boolean theatre) {
        this.theatre = theatre;
    }

    public boolean isCinema() {
        return cinema;
    }

    public void setCinema(boolean cinema) {
        this.cinema = cinema;
    }

    public boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public boolean isHotTub() {
        return hotTub;
    }

    public void setHotTub(boolean hotTub) {
        this.hotTub = hotTub;
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public void setRestaurant(boolean restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isLounges() {
        return lounges;
    }

    public void setLounges(boolean lounges) {
        this.lounges = lounges;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isBar() {
        return bar;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isKidsPlayRoom() {
        return kidsPlayRoom;
    }

    public void setKidsPlayRoom(boolean kidsPlayRoom) {
        this.kidsPlayRoom = kidsPlayRoom;
    }

    @Override
    public String toString() {
        return "ShipFacilities{" +
                "casino=" + casino +
                ", shops=" + shops +
                ", spa=" + spa +
                ", fitnessCenter=" + fitnessCenter +
                ", library=" + library +
                ", theatre=" + theatre +
                ", cinema=" + cinema +
                ", swimmingPool=" + swimmingPool +
                ", hotTub=" + hotTub +
                ", restaurant=" + restaurant +
                ", lounges=" + lounges +
                ", gym=" + gym +
                ", bar=" + bar +
                ", wifi=" + wifi +
                ", kidsPlayRoom=" + kidsPlayRoom +
                '}';
    }
}
