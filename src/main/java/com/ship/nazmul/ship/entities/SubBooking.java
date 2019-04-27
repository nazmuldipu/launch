package com.ship.nazmul.ship.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class SubBooking implements Serializable {
    private Date date;
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

    public SubBooking(Date date, Seat seat) {
        this.date = date;
        this.seat = seat;
    }

    public SubBooking(Date date, int discount, int commission, Seat seat) {
        this.date = date;
        this.discount = discount;
        this.commission = commission;
        this.seat = seat;
        this.seatNumber = seat.getSeatNumber();

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
