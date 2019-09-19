//package com.ship.nazmul.ship.services.accountings.impl;
//
//import com.ship.nazmul.ship.commons.PageAttr;
//import com.ship.nazmul.ship.config.security.SecurityConfig;
//import com.ship.nazmul.ship.entities.Role;
//import com.ship.nazmul.ship.entities.Ship;
//import com.ship.nazmul.ship.entities.User;
//import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
//import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
//import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
//import com.ship.nazmul.ship.repositories.accountings.ShipCashBookRepository;
//import com.ship.nazmul.ship.services.ShipService;
//import com.ship.nazmul.ship.services.accountings.ShipCashBookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class ShipCashBookServiceImpl implements ShipCashBookService {
//    private final ShipCashBookRepository shipCashBookRepository;
//    private final ShipService shipService;
//
//    @Autowired
//    public ShipCashBookServiceImpl(ShipCashBookRepository shipCashBookRepository, ShipService shipService) {
//        this.shipCashBookRepository = shipCashBookRepository;
//        this.shipService = shipService;
//    }
//
//    @Override
//    public ShipCashBook save(ShipCashBook shipCashBook) {
//        return this.shipCashBookRepository.save(shipCashBook);
//    }
//
//    @Override
//    public ShipCashBook getShipCashBook(Long id) {
//        return this.shipCashBookRepository.findOne(id);
//    }
//
//    @Override
//    public List<ShipCashBook> getAllShipCashBook() {
//        return this.shipCashBookRepository.findAll();
//    }
//
//    @Override
//    public Page<ShipCashBook> getAllShipCashBook(int page) {
//        return this.shipCashBookRepository.findAll(PageAttr.getPageRequest(page));
//    }
//
//    @Override
//    public List<ShipCashBook> getShipCashbookForServiceAdmin(Long shipId) throws ForbiddenException {
//        User user = SecurityConfig.getCurrentUser();
//        if (user.getShips() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
//            throw new ForbiddenException("Access denied");
//        return this.shipCashBookRepository.findByShipId(shipId);
//    }
//
//    @Override
//    public Page<ShipCashBook> getShipCashbookForServiceAdmin(Long shipId, Integer page) throws ForbiddenException {
//        User user = SecurityConfig.getCurrentUser();
//        if (user.getShips() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
//            throw new ForbiddenException("Access denied");
//        if (page == null) {
//            page = (int) (this.shipCashBookRepository.countByShipId(shipId) / PageAttr.PAGE_SIZE) - 1;
//        }
//        return this.shipCashBookRepository.findByShipId(shipId, PageAttr.getPageRequestAsc(page));
//    }
//
//    @Override
//    public void deleteShipCashBook(Long id) {
//        this.shipCashBookRepository.delete(id);
//    }
//
//    @Override
//    public boolean isExists(Long id) {
//        return this.shipCashBookRepository.exists(id);
//    }
//
//    @Override
//    public ShipCashBook debitAmount(ShipCashBook shipCashBook) {
//        ShipCashBook lastCashbook = this.shipCashBookRepository.findFirstByShipIdOrderByIdDesc(shipCashBook.getShip().getId());
//        int lastBalance = lastCashbook == null ? 0 : lastCashbook.getBalance();
//        shipCashBook.setBalance(lastBalance + shipCashBook.getDebit() - shipCashBook.getCredit());
//        shipCashBook = this.save(shipCashBook);
//        return shipCashBook;
//    }
//
//    @Override
//    public ShipCashBook creditAmount(ShipCashBook shipCashBook) {
//        return null;
//    }
//
//    @Override
//    public ShipCashBook addShipCashbookEntry(Long shipId, Integer debit, Integer credit, String explanation) throws ForbiddenException, NotFoundException {
//        User user = SecurityConfig.getCurrentUser();
//        Ship ship = this.shipService.getOne(shipId);
//        if (!user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) || user.getShips() == null)
//            throw new ForbiddenException("Access denied");
//        ShipCashBook lastCashbook = this.shipCashBookRepository.findFirstByShipIdOrderByIdDesc(shipId);
//        ShipCashBook shipCashBook = new ShipCashBook(ship, new Date(), explanation, debit, credit);
//        int lastBalance = lastCashbook == null ? 0 : lastCashbook.getBalance();
//        shipCashBook.setBalance(lastBalance + shipCashBook.getDebit() - shipCashBook.getCredit());
//        shipCashBook.setApproved(true);
//        shipCashBook = this.save(shipCashBook);
//        return shipCashBook;
//    }
//
//
//}
//
