package com.ship.nazmul.ship.entities.pojo;

import com.ship.nazmul.ship.entities.*;

import java.util.*;

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
    private boolean cancelled;
    private String category;
    private Seat.EStatus eStatus;

    public Ticket(Booking booking) {
        this.id = booking.getId();
        if(booking.isCancelled()){
            this.cancelled = true;
        } else {
            this.cancelled = false;
            this.created = booking.getCreated();
            this.createdBy = new User(booking.getCreatedBy().getName(), booking.getCreatedBy().getUsername(), booking.getCreatedBy().getPhoneNumber(), null);
            this.user = new User(booking.getUser().getName(), booking.getUser().getUsername(), booking.getUser().getPhoneNumber(), null);
            this.ship = new Ship(booking.getShip().getShipNumber(), booking.getShip().getName(), booking.getShip().getShipName(), booking.getShip().getStartingPoint(), booking.getShip().getDroppingPoint(), booking.getShip().getStartTime());
            this.totalFare = booking.getTotalFare();
            this.totalDiscount = booking.getTotalDiscount();
            this.totalCommission = booking.getTotalDiscount();
            this.eStatus = booking.geteStatus();
            //Populate subBookingList
            this.subBookingList = new ArrayList<>();
            final int[] categoryPrice = {0};
            final int[] categoryPriority = {0};
            booking.getSubBookingList().forEach(sb -> {
                SubBooking subBooking = new SubBooking(sb.getDate(), sb.getSeatNumber(), sb.getFare(), sb.getDiscount(), sb.getPayablePrice());
                this.subBookingList.add(subBooking);
                if(categoryPrice[0] == 0){
                    this.category = sb.getSeat().getCategory().getName();
                    categoryPrice[0] = sb.getSeat().getCategory().getFare();
                    categoryPriority[0] = sb.getSeat().getCategory().getPriority();
                } else if(categoryPrice[0] > sb.getSeat().getCategory().getFare()){
                    this.category = sb.getSeat().getCategory().getName();
                    categoryPrice[0] = sb.getSeat().getCategory().getFare();
                    categoryPriority[0] = sb.getSeat().getCategory().getPriority();
                } else if(categoryPrice[0] == sb.getSeat().getCategory().getFare()){
                    if(categoryPriority[0] > sb.getSeat().getCategory().getPriority()){
                        categoryPrice[0] = sb.getSeat().getCategory().getFare();
                        this.category = sb.getSeat().getCategory().getName();
                    }
                }
//                this.category.add(sb.getSeat().getCategory().getName());
            });
        }
//        Iterator<String> itr = this.category.iterator();
//        while (itr.hasNext()) {
//            System.out.println("ITR >"+itr.next());
//        }
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Seat.EStatus geteStatus() {
        return eStatus;
    }

    public void seteStatus(Seat.EStatus eStatus) {
        this.eStatus = eStatus;
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
                ", cancelled=" + cancelled +
                ", category=" + category +
                ", eStatus=" + eStatus +
                '}';
    }
}
