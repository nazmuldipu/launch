package com.ship.nazmul.ship.entities;

import com.ship.nazmul.ship.entities.base.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Seat extends BaseEntity{
    private String seatNumber;
    private int fare;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
