package com.ship.nazmul.ship.entities.pojo;

import java.util.Date;

public class ServiceAdminSellsReportRange {
    private Date date;
    private String shipNumber;
    private String shipName;
    private int totalNumberOfSeat;
    private int totalFare;
    private int totalAdvance;
    private int totalDue;
    private int totalCommission;
    private String status = "Unpaid";

    public ServiceAdminSellsReportRange() {
    }

    public ServiceAdminSellsReportRange(Date date, String shipNumber, String shipName, int totalNumberOfSeat, int totalFare) {
        this.date = date;
        this.shipNumber = shipNumber;
        this.shipName = shipName;
        this.totalNumberOfSeat = totalNumberOfSeat;
        this.totalFare = totalFare;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getShipNumber() {
        return shipNumber;
    }

    public void setShipNumber(String shipNumber) {
        this.shipNumber = shipNumber;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public int getTotalNumberOfSeat() {
        return totalNumberOfSeat;
    }

    public void setTotalNumberOfSeat(int totalNumberOfSeat) {
        this.totalNumberOfSeat = totalNumberOfSeat;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(int totalFare) {
        this.totalFare = totalFare;
    }

    public int getTotalAdvance() {
        return totalAdvance;
    }

    public void setTotalAdvance(int totalAdvance) {
        this.totalAdvance = totalAdvance;
    }

    public int getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(int totalDue) {
        this.totalDue = totalDue;
    }

    public int getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(int totalCommission) {
        this.totalCommission = totalCommission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ServiceAdminSellsReportRange{" +
                "date=" + date +
                ", shipNumber='" + shipNumber + '\'' +
                ", shipName='" + shipName + '\'' +
                ", totalNumberOfSeat=" + totalNumberOfSeat +
                ", totalFare=" + totalFare +
                ", totalAdvance=" + totalAdvance +
                ", totalDue=" + totalDue +
                ", totalCommission=" + totalCommission +
                ", status='" + status + '\'' +
                '}';
    }
}
