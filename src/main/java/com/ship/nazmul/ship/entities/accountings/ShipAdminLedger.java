package com.ship.nazmul.ship.entities.accountings;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class ShipAdminLedger extends BaseEntity {
//    @ManyToOne
//    private Ship ship;
    @ManyToOne
    private User user;
    private Date date;
    private String explanation;
    private String ref;
    private int debit;
    private int credit;
    private int balance;
    private boolean approved;

    public ShipAdminLedger() {
    }

    public ShipAdminLedger(User user, Date date, String explanation, int debit, int credit) {
//        this.ship = ship;
        this.user = user;
        this.date = date;
        this.explanation = explanation;
        this.debit = debit;
        this.credit = credit;
    }

//    public Ship getShip() {
//        return ship;
//    }
//
//    public void setShip(Ship ship) {
//        this.ship = ship;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "ShipAdminLedger{" +
                ", user=" + user +
                ", date=" + date +
                ", explanation='" + explanation + '\'' +
                ", ref='" + ref + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", balance=" + balance +
                ", approved=" + approved +
                '}';
    }
}
