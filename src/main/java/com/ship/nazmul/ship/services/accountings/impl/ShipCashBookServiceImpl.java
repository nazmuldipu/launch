package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.repositories.accountings.ShipCashBookRepository;
import com.ship.nazmul.ship.services.accountings.ShipCashBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShipCashBookServiceImpl implements ShipCashBookService {
    private final ShipCashBookRepository shipCashBookRepository;

    @Autowired
    public ShipCashBookServiceImpl(ShipCashBookRepository shipCashBookRepository) {
        this.shipCashBookRepository = shipCashBookRepository;
    }

    @Override
    public ShipCashBook save(ShipCashBook shipCashBook) {
        return this.shipCashBookRepository.save(shipCashBook);
    }

    @Override
    public ShipCashBook getShipCashBook(Long id) {
        return this.shipCashBookRepository.findOne(id);
    }

    @Override
    public List<ShipCashBook> getAllShipCashBook() {
        return this.shipCashBookRepository.findAll();
    }

    @Override
    public Page<ShipCashBook> getAllShipCashBook(int page) {
        return this.shipCashBookRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public List<ShipCashBook> getAllShipCashbookForServiceAdmin() throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (user.getShip() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");
        return this.shipCashBookRepository.findByShipId(user.getShip().getId());
    }

    @Override
    public Page<ShipCashBook> getAllShipCashbookForServiceAdmin(Integer page) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (user.getShip() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");
        if (page == null) {
            page = (int) (this.shipCashBookRepository.countByShipId(user.getShip().getId()) / PageAttr.PAGE_SIZE) - 1;
        }
        return this.shipCashBookRepository.findByShipId(user.getShip().getId(), PageAttr.getPageRequestAsc(page));
    }

    @Override
    public void deleteShipCashBook(Long id) {
        this.shipCashBookRepository.delete(id);
    }

    @Override
    public boolean isExists(Long id) {
        return this.shipCashBookRepository.exists(id);
    }

    @Override
    public ShipCashBook debitAmount(ShipCashBook shipCashBook) {
        ShipCashBook lastCashbook = this.shipCashBookRepository.findFirstByShipIdOrderByIdDesc(shipCashBook.getShip().getId());
        int lastBalance = lastCashbook == null ? 0 : lastCashbook.getBalance();
        shipCashBook.setBalance(lastBalance + shipCashBook.getDebit() - shipCashBook.getCredit());
        shipCashBook = this.save(shipCashBook);
        return shipCashBook;
    }

    @Override
    public ShipCashBook creditAmount(ShipCashBook shipCashBook) {
        return null;
    }

    @Override
    public ShipCashBook addShipCashbookEntry(Integer debit, Integer credit, String explanation) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) || user.getShip() == null)
            throw new ForbiddenException("Access denied");
        ShipCashBook lastCashbook = this.shipCashBookRepository.findFirstByShipIdOrderByIdDesc(user.getShip().getId());
        ShipCashBook shipCashBook = new ShipCashBook(user.getShip(), new Date(), explanation, debit, credit);
        int lastBalance = lastCashbook == null ? 0 : lastCashbook.getBalance();
        shipCashBook.setBalance(lastBalance + shipCashBook.getDebit() - shipCashBook.getCredit());
        shipCashBook.setApproved(true);
        shipCashBook = this.save(shipCashBook);
        return shipCashBook;
    }


}

