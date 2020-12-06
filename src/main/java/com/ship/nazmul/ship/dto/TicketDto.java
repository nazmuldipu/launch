package com.ship.nazmul.ship.dto;

import com.ship.nazmul.ship.entities.Seat;
import com.ship.nazmul.ship.entities.SubBooking;

import java.util.Date;
import java.util.List;

public class TicketDto {
    private Long id;
    private Date created;
    private UserDto createdBy;
    private UserDto user;
    private ShipDto ship;
    private int totalFare;
    private int totalDiscount;
    private int totalCommission;
    private String category;
    private Seat.EStatus eStatus;
    private List<SubBooking> subBookingList;

    private boolean confirmed;
    private boolean approved;
    private boolean paid;
    private boolean cancelled;

    public TicketDto() {
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

    public UserDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDto createdBy) {
        this.createdBy = createdBy;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ShipDto getShip() {
        return ship;
    }

    public void setShip(ShipDto ship) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Seat.EStatus geteStatus() {
        return eStatus;
    }

    public void seteStatus(Seat.EStatus eStatus) {
        this.eStatus = eStatus;
    }

    public List<SubBooking> getSubBookingList() {
        return subBookingList;
    }

    public void setSubBookingList(List<SubBooking> subBookingList) {
        this.subBookingList = subBookingList;
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
}
