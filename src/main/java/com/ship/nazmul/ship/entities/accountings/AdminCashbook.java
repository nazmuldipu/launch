package com.ship.nazmul.ship.entities.accountings;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeAttributeConverter;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeDeserializer;
import com.ship.nazmul.ship.commons.utils.LocalDateTimeSerializer;
import com.ship.nazmul.ship.entities.base.BaseEntity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class AdminCashbook extends BaseEntity {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime date;
    private String explanation;
    private String ref;
    private int debit;
    private int credit;
    private int balance;
    private boolean approved;

    public AdminCashbook() {
    }

    public AdminCashbook(LocalDateTime date, String explanation, int debit, int credit) {
        this.date = date;
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
        return "AdminCashbook{" +
                "date=" + date +
                ", explanation='" + explanation + '\'' +
                ", ref='" + ref + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", balance=" + balance +
                ", approved=" + approved +
                '}';
    }
}
