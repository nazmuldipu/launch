package com.ship.nazmul.ship.entities.accountings;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeAttributeConverter;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeDeserializer;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeSerializer;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.base.BaseEntity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ShipAdminCashbook extends BaseEntity {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date;
    @ManyToOne
    private User user;
    private String explanation;
    private String ref;
    private TransactionType type;
    private int debit;
    private int credit;
    private int balance;
    private boolean approved;

    public enum TransactionType {
        AGENT_BALANCE("Agent balance"),
        BOOKING("Booking"),
        CANCEL_BOOKING("Cancel Booking"),
        OTHER ("Other");

        String value;

        TransactionType (String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public ShipAdminCashbook() {
    }

    public ShipAdminCashbook(LocalDateTime date, User user, String explanation, int debit, int credit) {
        this.date = date;
        this.user = user;
        this.explanation = explanation;
        this.debit = debit;
        this.credit = credit;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ShipAdminCashbook{" +
                "date=" + date +
                ", user=" + user +
                ", explanation='" + explanation + '\'' +
                ", ref='" + ref + '\'' +
                ", type=" + type +
                ", debit=" + debit +
                ", credit=" + credit +
                ", balance=" + balance +
                ", approved=" + approved +
                '}';
    }
}
