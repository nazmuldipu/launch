package com.ship.nazmul.ship.dto;

import com.ship.nazmul.ship.entities.Ship;

public class ShipDto {
    private String shipNumber;
    private Ship.ShipName shipName;
    private String name;
    private String startingPoint;
    private String droppingPoint;
    private String startTime;


    public ShipDto() {
    }

    public ShipDto(String shipNumber, Ship.ShipName shipName, String name, String startingPoint, String droppingPoint, String startTime) {
        this.shipNumber = shipNumber;
        this.shipName = shipName;
        this.name = name;
        this.startingPoint = startingPoint;
        this.droppingPoint = droppingPoint;
        this.startTime = startTime;
    }

    public String getShipNumber() {
        return shipNumber;
    }

    public void setShipNumber(String shipNumber) {
        this.shipNumber = shipNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ship.ShipName getShipName() {
        return shipName;
    }

    public void setShipName(Ship.ShipName shipName) {
        this.shipName = shipName;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDroppingPoint() {
        return droppingPoint;
    }

    public void setDroppingPoint(String droppingPoint) {
        this.droppingPoint = droppingPoint;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
