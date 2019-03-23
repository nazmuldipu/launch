package com.ship.nazmul.ship.entities;

import javax.persistence.Embeddable;

@Embeddable
public class CategoryFacilities {
    private boolean topFloor;
    private boolean ac;
    private boolean freeBreakfast;

    public boolean isTopFloor() {
        return topFloor;
    }

    public void setTopFloor(boolean topFloor) {
        this.topFloor = topFloor;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean isFreeBreakfast() {
        return freeBreakfast;
    }

    public void setFreeBreakfast(boolean freeBreakfast) {
        this.freeBreakfast = freeBreakfast;
    }

    @Override
    public String toString() {
        return "CategoryFacilities{" +
                "topFloor=" + topFloor +
                ", ac=" + ac +
                ", freeBreakfast=" + freeBreakfast +
                '}';
    }
}
