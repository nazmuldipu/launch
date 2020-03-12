package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.utils.DateUtil;
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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

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
        if (!user.isAdmin())
            throw new ForbiddenException("Access denied");

        if (ship.getShipFacilities() == null) ship.setShipFacilities(new ShipFacilities());
        if (ship.getId() != null) {
            Ship oldShipObject = this.getOne(ship.getId());
            ship.setShipMap(oldShipObject.getShipMap());
            ship.setCategoryList(oldShipObject.getCategoryList());
            ship.setAdmin(oldShipObject.getAdmin());
//            List<String> imagePaths = this.getOne(ship.getId()).getImagePaths();
//            ship.setImagePaths(imagePaths);
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
        return this.shipRepository.findDistinctByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrStartingPointContainingIgnoreCaseOrDroppingPointContainingIgnoreCaseAndDeletedFalse(query, query, query, PageAttr.getPageRequest(page));
    }

    /*this method will find Ship running map from a date range
    * @param shipId     id of ship the map is looking for
    * @param startDate  start date of the map that is looking for
    * @param endDate    end date fo the map that is looking for
    * @return  map<date, boolean>  map of ship that is looking for*/
    @Override
    public Map<LocalDate, Boolean> getShipMap(Long shipId, LocalDate startDate, LocalDate endDate) throws NotFoundException {
        Ship ship = this.getOne(shipId);
        List<LocalDate> dates = DateUtil.getLocalDatesBetween(startDate, endDate);
        Map<LocalDate, Boolean> shipMap = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            Boolean shipM = ship.getShipMap().get(dates.get(i));
            if(shipM == null) shipM = false;
            shipMap.put(dates.get(i), shipM);
        }
        return shipMap;
    }

    @Override
    public boolean isShipActive(Long shipId, LocalDate date) throws NotFoundException {
        Ship ship = this.getOne(shipId);
        Boolean shipM = ship.getShipMap().get(date);

        if(shipM == null) shipM = false;

        return shipM;
    }

    @Override
    public boolean updateShipMap(Long shipId, LocalDate date, Boolean value) throws NotFoundException, ForbiddenException, InvalidException {
        Ship ship = this.getOne(shipId);
//        Boolean shipM = ship.getShipMap().get(date);
        ship.getShipMap().put(date, value);
        this.save(ship);
        return value;
    }

    @Override
    @Transactional
    public List<Ship> getMyShips() {
        User user = SecurityConfig.getCurrentUser();
        List<Ship> shipSet = new ArrayList<>();
        Iterator<Ship> shipIterator = user.getShips().iterator();
        while(shipIterator.hasNext()){
            Ship ship = shipIterator.next();
//            ship.setImagePaths(null);
            ship.setCategoryList(null);
            shipSet.add(ship);
        }
        return shipSet;

//        if (user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) || user.hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString())) {
//            List<Ship> shipSet = new ArrayList<>();
//            Iterator<Ship> shipIterator = user.getShips().iterator();
//            while(shipIterator.hasNext()){
//                Ship ship = shipIterator.next();
//                ship.setImagePaths(null);
//                ship.setCategoryList(null);
//                shipSet.add(ship);
//            }
//
//            return user.getShips();
//        }
//        return null;
    }
}
