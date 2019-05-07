package com.ship.nazmul.ship.commons;

import com.ship.nazmul.ship.entities.Ship;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

public class Validator {
    public static boolean containsShip(Set<Ship> shipSet, Ship ship) {
        for(Ship s : shipSet){
            if(ship.getId() == s.getId())
                return true;
        }
        return false;
    }
}
