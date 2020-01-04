package com.ship.nazmul.ship.entities.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ship.nazmul.ship.commons.utils.LocalDateDeserializer;
import com.ship.nazmul.ship.commons.utils.LocalDateSerializer;
import com.ship.nazmul.ship.entities.Seat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

public class ServiceAdminSellsReport {
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate journeyDate;
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
    private int hotelswaveCommission;
    private int hotelswaveAgentCommission;
    private int shipAgentCommission;
    private String soldBy;
    private String role;
    private boolean paid;



    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
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

    public int getHotelswaveCommission() {
        return hotelswaveCommission;
    }

    public void setHotelswaveCommission(int hotelswaveCommission) {
        this.hotelswaveCommission = hotelswaveCommission;
    }

    public int getHotelswaveAgentCommission() {
        return hotelswaveAgentCommission;
    }

    public void setHotelswaveAgentCommission(int hotelswaveAgentCommission) {
        this.hotelswaveAgentCommission = hotelswaveAgentCommission;
    }

    public int getShipAgentCommission() {
        return shipAgentCommission;
    }

    public void setShipAgentCommission(int shipAgentCommission) {
        this.shipAgentCommission = shipAgentCommission;
    }

    @Override
    public String toString() {
        return "ServiceAdminSellsReport{" +
                "journeyDate=" + journeyDate +
                ", bookingId=" + bookingId +
                ", bookingStatus=" + bookingStatus +
                ", bookingDate=" + bookingDate +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipNumber='" + shipNumber + '\'' +
                ", routes='" + routes + '\'' +
                ", seatNumbers=" + Arrays.toString(seatNumbers) +
                ", price=" + price +
                ", hotelswaveCommission=" + hotelswaveCommission +
                ", hotelswaveAgentCommission=" + hotelswaveAgentCommission +
                ", shipAgentCommission=" + shipAgentCommission +
                ", soldBy='" + soldBy + '\'' +
                ", role='" + role + '\'' +
                ", paid=" + paid +
                '}';
    }
}
