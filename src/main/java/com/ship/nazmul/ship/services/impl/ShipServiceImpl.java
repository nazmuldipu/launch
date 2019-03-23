package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.ShipFacilities;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.repositories.ShipRepository;
import com.ship.nazmul.ship.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Ship save(Ship ship) throws ForbiddenException, InvalidException, NotFoundException {
        //Security check
        if (ship == null) throw new InvalidException("Ship can not be null");
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin() && !(user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) && user.getShip().getId() == ship.getId()))
            throw new ForbiddenException("Access denied");

        if (ship.getShipFacilities() == null) ship.setShipFacilities(new ShipFacilities());
        if (ship.getId() != null) {
            List<String> imagePaths = this.getOne(ship.getId()).getImagePaths();
            ship.setImagePaths(imagePaths);
            //TODO: setStartsFrom
//            ship.setStartsFrom();
        }
        return this.shipRepository.save(ship);
    }

    @Override
    public Ship getOne(long id) throws NotFoundException {
        if (!this.exists(id)) throw new NotFoundException("Hotel with this ID doesn't exists");
        return this.shipRepository.findOne(id);
    }

    @Override
    public List<Ship> getAll() {
        return this.shipRepository.findByDeletedFalse();
    }

    @Override
    public Page<Ship> getAll(int page) {
        return this.shipRepository.findByDeletedFalse(PageAttr.getPageRequest(page));
    }

    @Override
    public boolean exists(long id) {
        return this.shipRepository.exists(id);
    }

    @Override
    public void delete(long id) throws NotFoundException, ForbiddenException, InvalidException {
        Ship ship = this.getOne(id);
        ship.setDeleted(true);
        this.save(ship);
    }

    @Override
    public Page<Ship> searchShip(String query, int page) {
        return this.shipRepository.findDistinctByNameContainingIgnoreCaseOrStartingPointContainingIgnoreCaseOrDroppingPointContainingIgnoreCaseAndDeletedFalse(query, query, query, PageAttr.getPageRequest(page));
    }
}
