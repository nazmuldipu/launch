package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.accountings.ShipAdminCashbook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.repositories.accountings.ShipAdminCashbookRepository;
import com.ship.nazmul.ship.services.ShipService;
import com.ship.nazmul.ship.services.UserService;
import com.ship.nazmul.ship.services.accountings.ShipAdminCashbookService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ShipAdminCashbookServiceImpl implements ShipAdminCashbookService {
    private final ShipAdminCashbookRepository shipAdminCashbookRepository;
    private final ShipService shipService;
    private final UserService userService;

    @Autowired
    public ShipAdminCashbookServiceImpl(ShipAdminCashbookRepository shipAdminCashbookRepository, ShipService shipService, UserService userService) {
        this.shipAdminCashbookRepository = shipAdminCashbookRepository;
        this.shipService = shipService;
        this.userService = userService;
    }

    @Override
    public ShipAdminCashbook save(ShipAdminCashbook shipAdminCashbook) {
        return this.shipAdminCashbookRepository.save(shipAdminCashbook);
    }

    @Override
    public ShipAdminCashbook getShipAdminCashBook(Long id) {
        return this.shipAdminCashbookRepository.findOne(id);
    }

    @Override
    public List<ShipAdminCashbook> getAllShipAdminCashBook() {
        return this.shipAdminCashbookRepository.findAll();
    }

    @Override
    public Page<ShipAdminCashbook> getAllShipAdminCashBook(int page) {
        return this.shipAdminCashbookRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public List<ShipAdminCashbook> getShipAdminCashbookForServiceAdmin(Long userId) {
        return this.shipAdminCashbookRepository.findByUserId(userId);
    }

    @Override
    public Page<ShipAdminCashbook> getShipAdminCashbookForServiceAdmin(Long userId, Integer page) {
        return this.shipAdminCashbookRepository.findByUserIdOrderByIdAsc(userId, PageAttr.getPageRequest(page));
    }

    @Override
    public void deleteShipAdminCashBook(Long id) {
        this.shipAdminCashbookRepository.delete(id);
    }

    @Override
    public boolean isExists(Long id) {
        return this.shipAdminCashbookRepository.exists(id);
    }

    @Override
    public ShipAdminCashbook debitAmount(ShipAdminCashbook shipAdminCashbook) {
//        ShipCashBook lastCashbook = this.shipCashBookRepository.findFirstByShipIdOrderByIdDesc(shipCashBook.getShip().getId());
//        int lastBalance = lastCashbook == null ? 0 : lastCashbook.getBalance();
//        shipCashBook.setBalance(lastBalance + shipCashBook.getDebit() - shipCashBook.getCredit());
//        shipCashBook = this.save(shipCashBook);
//        return shipCashBook;
        int lastBalance = this.getLastShipAdminLastEntryBalance(shipAdminCashbook.getUser().getId());
        shipAdminCashbook.setBalance(lastBalance + shipAdminCashbook.getDebit() - shipAdminCashbook.getCredit());
        shipAdminCashbook = this.save(shipAdminCashbook);
        return shipAdminCashbook;
    }

    @Override
    public ShipAdminCashbook creditAmount(ShipAdminCashbook shipCashBook) {
        return null;
    }

    @Override
    public ShipAdminCashbook addShipAdminCashbookEntry(Long userId, Integer debit, Integer credit, String explanation) throws ForbiddenException, UserNotFoundException {
        User currentUser = SecurityConfig.getCurrentUser();
        User user = this.userService.getOne(userId);
        if (!currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) || currentUser.getShips() == null)
            throw new ForbiddenException("Access denied");

        ShipAdminCashbook shipAdminCashbook = new ShipAdminCashbook(LocalDate.now(), user, explanation, debit, credit);
        int lastBalance = this.getLastShipAdminLastEntryBalance(userId);
        shipAdminCashbook.setBalance(lastBalance+shipAdminCashbook.getDebit()-shipAdminCashbook.getCredit());
        shipAdminCashbook.setApproved(true);
        shipAdminCashbook = this.save(shipAdminCashbook);
        return shipAdminCashbook;
    }

    private int getLastShipAdminLastEntryBalance(Long userId){
        ShipAdminCashbook lastShipAdminCashbook = this.shipAdminCashbookRepository.findFirstByUserIdOrderByIdDesc(userId);
        return lastShipAdminCashbook == null ? 0 : lastShipAdminCashbook.getBalance();
    }
}
