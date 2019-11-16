package com.ship.nazmul.ship.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ship.nazmul.ship.commons.utils.LocalDateAttributeConverter;
import com.ship.nazmul.ship.entities.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Entity
public class Seat extends BaseEntity{
    private String seatNumber;
//    private int fare;
//    private int discount;
//    private int agentDiscount;
    private boolean discounted;
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Category category;

    @ManyToOne
    @JsonBackReference
    private Ship ship;



    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "date")
    @Convert(converter = LocalDateAttributeConverter.class, attributeName = "key")
    private Map<LocalDate, EStatus> seatStatusMap;


    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "date")
    @Convert(converter = LocalDateAttributeConverter.class, attributeName = "key")
    private Map<LocalDate, Long> bookingIdMap;

    public enum EStatus {
        SEAT_BLOCKED("Blocked"),
        SEAT_RESERVED("Reserved"),
        SEAT_SOLD("Sold"),
        SEAT_FREE("Free");

        String value;

        EStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

//    @PrePersist
//    @PreUpdate
//    public void setDiscounted() {
//        if (this.discount > 0)
//            this.setDiscounted(true);
//        else this.setDiscounted(false);
//    }
//
//    public int getDiscountedPrice(Date date) {
//        int x = this.fare - this.getDiscount(date);
//        if (x < 0)
//            return 0;
//        return x;
//    }
//
//    public int getDiscount(Date date) {
//        int discount = 0;
//        if (this.discountMap != null)
//            for (Map.Entry<Date, Integer> entry : discountMap.entrySet()) {
//                // todays discount from map
//                if (DateUtil.isSameDay(entry.getKey(), date))
//                    // check if discount exceeds the original price
//                    if (entry.getValue() < this.fare) {
//                        discount = entry.getValue();
//                        break;
//                    }
//            }
//
//        // check if discount is still zero. assign default discount amount if true
//        if (discount <= 0) discount = this.discount;
//
//        // for agent add their special discount with original discount;
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return discount;
//    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

//    public int getFare() {
//        return fare;
//    }
//
//    public void setFare(int fare) {
//        this.fare = fare;
//    }
//
//    public int getDiscount() {
//        return discount;
//    }
//
//    public void setDiscount(int discount) {
//        this.discount = discount;
//    }
//
//    public int getAgentDiscount() {
//        return agentDiscount;
//    }
//
//    public void setAgentDiscount(int agentDiscount) {
//        this.agentDiscount = agentDiscount;
//    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Map<LocalDate, EStatus> getSeatStatusMap() {
        return seatStatusMap;
    }

    public void setSeatStatusMap(Map<LocalDate, EStatus> seatStatusMap) {
        this.seatStatusMap = seatStatusMap;
    }

    public Map<LocalDate, Long> getBookingIdMap() {
        return bookingIdMap;
    }

    public void setBookingIdMap(Map<LocalDate, Long> bookingIdMap) {
        this.bookingIdMap = bookingIdMap;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNumber='" + seatNumber + '\'' +
                ", discounted=" + discounted +
                ", available=" + available +
                ", category=" + category +
                ", ship=" + ship +
                ", seatStatusMap=" + seatStatusMap +
                ", bookingIdMap=" + bookingIdMap +
                '}';
    }
}
