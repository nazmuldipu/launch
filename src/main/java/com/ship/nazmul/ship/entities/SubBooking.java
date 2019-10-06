package com.ship.nazmul.ship.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ship.nazmul.ship.commons.utils.LocalDateDeserializer;
import com.ship.nazmul.ship.commons.utils.LocalDateSerializer;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class SubBooking implements Serializable {
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    private String seatNumber;
    private int fare;
    private int discount;
    private int commission;
    private int payablePrice;
    private boolean paid;

    @OneToOne
    @JsonBackReference
//    @JsonIgnore
    private Seat seat;


    public SubBooking() {
    }

    public SubBooking(LocalDate date, Seat seat) {
        this.date = date;
        this.seat = seat;
    }

    public SubBooking(LocalDate date, int discount, int commission, Seat seat) {
        this.date = date;
        this.discount = discount;
        this.commission = commission;
        this.seat = seat;
        this.seatNumber = seat.getSeatNumber();

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
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

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(int payablePrice) {
        this.payablePrice = payablePrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "SubBooking{" +
                "date=" + date +
                ", payablePrice=" + payablePrice +
                ", discount=" + discount +
                ", commission=" + commission +
                ", paid=" + paid +
                ", seat=" + seat +
                '}';
    }
}
