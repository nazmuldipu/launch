package com.ship.nazmul.ship.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ship.nazmul.ship.entities.base.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category extends BaseEntity {
    private String name;
    private String description;
    private String seatQuality;
    private int floorNumber;

    @JsonIgnore
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> imagePaths;

    @Embedded
    private CategoryFacilities categoryFacilities;

    @ManyToOne
    private Ship ship;

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

    public CategoryFacilities getCategoryFacilities() {
        return categoryFacilities;
    }

    public void setCategoryFacilities(CategoryFacilities categoryFacilities) {
        this.categoryFacilities = categoryFacilities;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", seatQuality='" + seatQuality + '\'' +
                ", floorNumber=" + floorNumber +
                ", imagePaths=" + imagePaths +
                ", categoryFacilities=" + categoryFacilities +
                ", ship=" + ship +
                '}';
    }
}
