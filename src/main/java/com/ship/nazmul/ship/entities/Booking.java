package com.ship.nazmul.ship.entities;

import com.ship.nazmul.ship.entities.base.BaseEntity;
import com.ship.nazmul.ship.ssl.parametermappings.SSLCommerzValidatorResponse;

import javax.persistence.*;
import java.util.List;

@Entity
public class Booking extends BaseEntity {
    @ElementCollection
    private List<SubBooking> subBookingList;

    @OneToOne
    private Ship ship;

    private String refBy;
    private int totalFare;
    private int totalDiscount;
    private int totalCommission;
    private int promotionDiscount;
    private int totalPayablePrice;

    private int agentDiscount;
    private int hotelswaveDiscount;
    private int hotelswaveAgentDiscount;

    private Seat.EStatus eStatus;

    private boolean manualBooking;
    private boolean confirmed;
    private boolean approved;
    private boolean paid;
    private boolean cancelled;
    @Embedded
    private SSLCommerzValidatorResponse sslCommerzValidatorResponse;

    @ManyToOne
    private User user;

    public List<SubBooking> getSubBookingList() {
        return subBookingList;
    }

    public void setSubBookingList(List<SubBooking> subBookingList) {
        this.subBookingList = subBookingList;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public String getRefBy() {
        return refBy;
    }

    public void setRefBy(String refBy) {
        this.refBy = refBy;
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

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(int promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public int getTotalPayablePrice() {
        return totalPayablePrice;
    }

    public void setTotalPayablePrice(int totalPayablePrice) {
        this.totalPayablePrice = totalPayablePrice;
    }

    public int getAgentDiscount() {
        return agentDiscount;
    }

    public void setAgentDiscount(int agentDiscount) {
        this.agentDiscount = agentDiscount;
    }

    public int getHotelswaveDiscount() {
        return hotelswaveDiscount;
    }

    public void setHotelswaveDiscount(int hotelswaveDiscount) {
        this.hotelswaveDiscount = hotelswaveDiscount;
    }

    public int getHotelswaveAgentDiscount() {
        return hotelswaveAgentDiscount;
    }

    public void setHotelswaveAgentDiscount(int hotelswaveAgentDiscount) {
        this.hotelswaveAgentDiscount = hotelswaveAgentDiscount;
    }

    public Seat.EStatus geteStatus() {
        return eStatus;
    }

    public void seteStatus(Seat.EStatus eStatus) {
        this.eStatus = eStatus;
    }

    public boolean isManualBooking() {
        return manualBooking;
    }

    public void setManualBooking(boolean manualBooking) {
        this.manualBooking = manualBooking;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public SSLCommerzValidatorResponse getSslCommerzValidatorResponse() {
        return sslCommerzValidatorResponse;
    }

    public void setSslCommerzValidatorResponse(SSLCommerzValidatorResponse sslCommerzValidatorResponse) {
        this.sslCommerzValidatorResponse = sslCommerzValidatorResponse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "subBookingList=" + subBookingList +
                ", ship=" + ship +
                ", refBy='" + refBy + '\'' +
                ", totalFare=" + totalFare +
                ", totalDiscount=" + totalDiscount +
                ", totalCommission=" + totalCommission +
                ", promotionDiscount=" + promotionDiscount +
                ", totalPayablePrice=" + totalPayablePrice +
                ", agentDiscount=" + agentDiscount +
                ", hotelswaveDiscount=" + hotelswaveDiscount +
                ", hotelswaveAgentDiscount=" + hotelswaveAgentDiscount +
                ", eStatus=" + eStatus +
                ", manualBooking=" + manualBooking +
                ", confirmed=" + confirmed +
                ", approved=" + approved +
                ", paid=" + paid +
                ", cancelled=" + cancelled +
                ", sslCommerzValidatorResponse=" + sslCommerzValidatorResponse +
                ", user=" + user +
                '}';
    }
}
