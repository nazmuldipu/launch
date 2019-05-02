package com.ship.nazmul.ship.entities.pojo;

import com.ship.nazmul.ship.entities.Seat;

import java.util.Date;

public class ServiceAdminSellsReport {
    private Date journeyDate;
    private Long bookingId;
    private Seat.EStatus bookingStatus;
    private Date bookingDate;
    private String customerName;
    private String customerPhone;
    private String shipName;
    private String shipNumber;
    private String routes;
    private String seatNumbers[];
    private int price;
    private String soldBy;
    private String role;
    private boolean paid;



    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Seat.EStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Seat.EStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipNumber() {
        return shipNumber;
    }

    public void setShipNumber(String shipNumber) {
        this.shipNumber = shipNumber;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public String[] getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String[] seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
