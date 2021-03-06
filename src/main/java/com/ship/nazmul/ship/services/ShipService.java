package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ShipService {
    Ship save(Ship ship) throws ForbiddenException, InvalidException, NotFoundException;

    Ship getOne(long id) throws NotFoundException;

    List<Ship> getAll();

    Page<Ship> getAll(int page);

    boolean exists(long id);

    void delete(long id) throws NotFoundException, ForbiddenException, InvalidException;

    Page<Ship> searchShip(String query, int page);

    Map<LocalDate, Boolean> getShipMap(Long shipId, LocalDate startDate, LocalDate endDate) throws NotFoundException;

    boolean isShipActive(Long shipId, LocalDate date) throws NotFoundException;

    boolean updateShipMap(Long shipId, LocalDate date, Boolean value) throws NotFoundException, ForbiddenException, InvalidException;

    // ************************ SERVICE ADMIN MODULES ******************
    List<Ship> getMyShips();
}
