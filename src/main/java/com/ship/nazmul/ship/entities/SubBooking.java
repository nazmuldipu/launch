package com.ship.nazmul.ship.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ship.nazmul.ship.commons.utils.LocalDateAttributeConverter;
import com.ship.nazmul.ship.commons.utils.LocalDateDeserializer;
import com.ship.nazmul.ship.commons.utils.LocalDateSerializer;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeAttributeConverter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class SubBooking implements Serializable {
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date;
    private String seatNumber;
    private int fare;
    private int discount;
    private int commission;
    private int payablePrice;
    private boolean paid;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Seat seat;

    private Long categoryId;
    private Long seatId;


    public SubBooking() {
    }

    public SubBooking(LocalDate date, Seat seat) {
        this.date = date;
        this.seat = seat;
    }

    public SubBooking(Seat seat) {
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    @Override
    public String toString() {
        return "SubBooking{" +
                "date=" + date +
                ", seatNumber='" + seatNumber + '\'' +
                ", fare=" + fare +
                ", discount=" + discount +
                ", commission=" + commission +
                ", payablePrice=" + payablePrice +
                ", paid=" + paid +
                ", seat=" + seat +
                ", categoryId=" + categoryId +
                ", seatId=" + seatId +
                '}';
    }
}
