package com.ship.nazmul.ship.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ship.nazmul.ship.commons.utils.LocalDateAttributeConverter;
import com.ship.nazmul.ship.entities.base.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Category extends BaseEntity {
    private String name;
    private String description;
    private String seatQuality;
    private int floorNumber;
    private int fare;
    private int discount;
    private int agentDiscount;
    private int priority;

    @JsonIgnore
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> imagePaths;

    @Embedded
    private CategoryFacilities categoryFacilities;

    @ManyToOne
    private Ship ship;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "date")
    @Convert(converter = LocalDateAttributeConverter.class, attributeName = "key")
    private Map<LocalDate, Integer> discountMap;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "date")
    @Convert(converter = LocalDateAttributeConverter.class, attributeName = "key")
    private Map<LocalDate, Integer> priceMap;
//    private Map<LocalDate, Integer> discountMap;


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

    public String getSeatQuality() {
        return seatQuality;
    }

    public void setSeatQuality(String seatQuality) {
        this.seatQuality = seatQuality;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void addImagePath(String imagePath) {
        if (this.imagePaths == null) this.imagePaths = new ArrayList<>();
        this.imagePaths.add(imagePath);
    }


    public Map<LocalDate, Integer> getDiscountMap() {
        return discountMap;
    }

    public void setDiscountMap(Map<LocalDate, Integer> discountMap) {
        this.discountMap = discountMap;
    }



    public CategoryFacilities getCategoryFacilities() {
        return categoryFacilities;
    }

    public void setCategoryFacilities(CategoryFacilities categoryFacilities) {
        this.categoryFacilities = categoryFacilities;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getAgentDiscount() {
        return agentDiscount;
    }

    public void setAgentDiscount(int agentDiscount) {
        this.agentDiscount = agentDiscount;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<LocalDate, Integer> getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map<LocalDate, Integer> priceMap) {
        this.priceMap = priceMap;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", seatQuality='" + seatQuality + '\'' +
                ", floorNumber=" + floorNumber +
                ", fare=" + fare +
                ", discount=" + discount +
                ", agentDiscount=" + agentDiscount +
                ", priority=" + priority +
                ", imagePaths=" + imagePaths +
                ", categoryFacilities=" + categoryFacilities +
                ", discountMap=" + discountMap +
                '}';
    }
}
