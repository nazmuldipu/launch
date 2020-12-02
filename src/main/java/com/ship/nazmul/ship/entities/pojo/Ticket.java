package com.ship.nazmul.ship.entities.pojo;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.SubBooking;
import com.ship.nazmul.ship.entities.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Ticket {
    private Long id;
    private Date created;
    private User createdBy;
    private List<SubBooking> subBookingList;
    private User user;
    private Ship ship;
    private int totalFare;
    private int totalDiscount;
    private int totalCommission;
    private String[] category;

    public Ticket(Booking booking) {
        this.id = booking.getId();
        this.created = booking.getCreated();
        this.createdBy = new User(booking.getCreatedBy().getName(), booking.getCreatedBy().getUsername(), booking.getCreatedBy().getPhoneNumber(), null);
//        this.subBookingList = booking.getSubBookingList();
        this.user = new User(booking.getUser().getName(), booking.getUser().getUsername(), booking.getUser().getPhoneNumber(), null);
//        this.ship = booking.getShip();
        this.totalFare = booking.getTotalFare();
        this.totalDiscount = booking.getTotalDiscount();
        this.totalCommission = booking.getTotalDiscount();
    }

    public Ticket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<SubBooking> getSubBookingList() {
        return subBookingList;
    }

    public void setSubBookingList(List<SubBooking> subBookingList) {
        this.subBookingList = subBookingList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(int totalFare) {
        this.totalFare = totalFare;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public int getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(int totalCommission) {
        this.totalCommission = totalCommission;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy=" + createdBy +
                ", subBookingList=" + subBookingList +
                ", user=" + user +
                ", ship=" + ship +
                ", totalFare=" + totalFare +
                ", totalDiscount=" + totalDiscount +
                ", totalCommission=" + totalCommission +
                ", category=" + Arrays.toString(category) +
                '}';
    }
}
