package com.ship.nazmul.ship.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ship.nazmul.ship.entities.base.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ship extends BaseEntity {
    private String shipNumber;
    private String name;
    @Column(length = 10000)
    private String description;
    private String kidsPolicy;
    private String phoneNumber;
    private String quality;//Premium, Delux, Regular
    private String floor;
    private String size;//Square feet
    private String startingPoint;
    private String droppingPoint;
    private String startTime;
    private String route;
    private ShipName shipName;
    private boolean ac;
    private boolean containCabin;
    private boolean online;
    private boolean enabled;
    private boolean deleted;

    private Integer discount;
    private int startsFrom;
    private float rating;
    private int numberOfReviews;

    private Integer hotelswaveCommission;

//    @JsonIgnore
//    @ElementCollection(fetch = FetchType.LAZY)
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    private List<String> imagePaths;

    @Embedded
    private ShipFacilities shipFacilities;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Category> categoryList;

    @ManyToOne
    private User admin;

    public enum ShipName{
        SUKANTO_BABU("Sukanto Babu"),
        BAY_CRUISE("Bay_Cruise"),
        OTHER("Other");

        String value;

        ShipName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<User> user;

    public String getShipNumber() {
        return shipNumber;
    }

    public void setShipNumber(String shipNumber) {
        this.shipNumber = shipNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKidsPolicy() {
        return kidsPolicy;
    }

    public void setKidsPolicy(String kidsPolicy) {
        this.kidsPolicy = kidsPolicy;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDroppingPoint() {
        return droppingPoint;
    }

    public void setDroppingPoint(String droppingPoint) {
        this.droppingPoint = droppingPoint;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean isContainCabin() {
        return containCabin;
    }

    public void setContainCabin(boolean containCabin) {
        this.containCabin = containCabin;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public int getStartsFrom() {
        return startsFrom;
    }

    public void setStartsFrom(int startsFrom) {
        this.startsFrom = startsFrom;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public Integer getHotelswaveCommission() {
        return hotelswaveCommission;
    }

    public void setHotelswaveCommission(Integer hotelswaveCommission) {
        this.hotelswaveCommission = hotelswaveCommission;
    }

    //    public List<String> getImagePaths() {
//        return imagePaths;
//    }
//
//    public void setImagePaths(List<String> imagePaths) {
//        this.imagePaths = imagePaths;
//    }

    public ShipFacilities getShipFacilities() {
        return shipFacilities;
    }

    public void setShipFacilities(ShipFacilities shipFacilities) {
        this.shipFacilities = shipFacilities;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public ShipName getShipName() {
        return shipName;
    }

    public void setShipName(ShipName shipName) {
        this.shipName = shipName;
    }
//    public List<User> getUser() {
//        return user;
//    }
//
//    public void setUser(List<User> user) {
//        this.user = user;
//    }


    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipNumber='" + shipNumber + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", kidsPolicy='" + kidsPolicy + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", quality='" + quality + '\'' +
                ", floor='" + floor + '\'' +
                ", size='" + size + '\'' +
                ", startingPoint='" + startingPoint + '\'' +
                ", droppingPoint='" + droppingPoint + '\'' +
                ", startTime='" + startTime + '\'' +
                ", route='" + route + '\'' +
                ", ac=" + ac +
                ", containCabin=" + containCabin +
                ", online=" + online +
                ", enabled=" + enabled +
                ", deleted=" + deleted +
                ", discount=" + discount +
                ", startsFrom=" + startsFrom +
                ", rating=" + rating +
                ", numberOfReviews=" + numberOfReviews +
                ", hotelswaveCommission=" + hotelswaveCommission +
//                ", imagePaths=" + imagePaths +
                ", shipFacilities=" + shipFacilities +
                '}';
    }
}
