package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.accountings.AdminShipLedger;
import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.repositories.accountings.AdminShipLedgerRepository;
import com.ship.nazmul.ship.services.ShipService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import com.ship.nazmul.ship.services.accountings.AdminShipLedgerService;
import com.ship.nazmul.ship.services.accountings.ShipCashBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminShipLedgerServiceImpl implements AdminShipLedgerService {
    private final AdminShipLedgerRepository adminShipLedgerRepository;
    private final AdminCashbookService adminCashbookService;
    private final com.ship.nazmul.ship.services.accountings.ShipCashBookService ShipCashBookService;
    private final com.ship.nazmul.ship.services.ShipService ShipService;

    @Autowired
    public AdminShipLedgerServiceImpl(AdminShipLedgerRepository adminShipLedgerRepository, AdminCashbookService adminCashbookService, ShipCashBookService ShipCashBookService, ShipService ShipService) {
        this.adminShipLedgerRepository = adminShipLedgerRepository;
        this.adminCashbookService = adminCashbookService;
        this.ShipCashBookService = ShipCashBookService;
        this.ShipService = ShipService;
    }

    @Override
    public AdminShipLedger save(AdminShipLedger adminAgentLedger) {
        return this.adminShipLedgerRepository.save(adminAgentLedger);
    }

    @Override
    public AdminShipLedger getAdminShipLedger(Long shipId) {
        return this.adminShipLedgerRepository.findOne(shipId);
    }

    @Override
    public List<AdminShipLedger> getAllAdminShipLedger() {
        return this.adminShipLedgerRepository.findAll();
    }

    @Override
    public Page<AdminShipLedger> getAllAdminShipLedger(int page) {
        return this.adminShipLedgerRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public List<AdminShipLedger> getAdminShipLedgerByShipId(Long shipId) {
        return this.adminShipLedgerRepository.findByShipId(shipId);
    }

    @Override
    public Page<AdminShipLedger> getAdminShipLedgerByShipId(Long shipId, Integer page) {

        if(page == null){
            page = (int) (this.adminShipLedgerRepository.countByShipId(shipId) / PageAttr.PAGE_SIZE) - 1;
            if(page < 0){
                page = 0;
            }
        }
        return this.adminShipLedgerRepository.findByShipId(shipId, PageAttr.getPageRequestAsc(page));
    }

    @Override
    public void deleteAdminShipLedger(Long ShipId) {
        this.adminShipLedgerRepository.delete(ShipId);
    }

    @Override
    public boolean isExists(Long id) {
        return this.adminShipLedgerRepository.exists(id);
    }

    @Override
    public AdminShipLedger updateAdminShipLedger(Long ShipId, AdminShipLedger adminShipLedger) {
        AdminShipLedger lastAdminShipLedger = this.adminShipLedgerRepository.findFirstByShipIdOrderByIdDesc(ShipId);
        int lastBalance = lastAdminShipLedger == null ? 0 : lastAdminShipLedger.getBalance();
        adminShipLedger.setBalance(lastBalance - adminShipLedger.getDebit() + adminShipLedger.getCredit());
        adminShipLedger = this.save(adminShipLedger);
        return adminShipLedger;
    }


    /* while Shipswave paying to Ship following 3 task must full fill
     * 1) Amount credit from AdminCashbook
     * 2) Amount debit from AdminShipLedger
     * 3) Amount debit to ShipCashbook
     * */
    @Override
    public AdminShipLedger payToShip(Long shipId, int amount) throws NotFoundException, ForbiddenException {
        Ship Ship = this.ShipService.getOne(shipId);
        // 1) Amount credit from AdminCashbook
        this.adminCashbookService.addAdminCashbookEntry(0,amount, "Pay to Ship : " + Ship.getName());

        // 2) Amount debit from AdminShipLedger
        AdminShipLedger adminShipLedger = new AdminShipLedger(Ship, new Date(), "Got payment from Shipswave ", amount, 0);
        adminShipLedger = this.updateAdminShipLedger(shipId, adminShipLedger);

        // 3) Amount debit to ShipCashbook
        ShipCashBook ShipCashBook = new ShipCashBook(Ship, new Date(), "Hotelswave payment ", amount, 0);
        ShipCashBook = this.ShipCashBookService.debitAmount(ShipCashBook);

        return adminShipLedger;
    }
    /*ShipCashBook lastCashbook = this.ShipCashBookRepository.findFirstByShipIdOrderByIdDesc(ShipCashBook.getShip().getId());
        int lastBalance = lastCashbook == null? 0 : lastCashbook.getBalance();
        ShipCashBook.setBalance(lastBalance + ShipCashBook.getDebit() - ShipCashBook.getCredit());
        ShipCashBook = this.save(ShipCashBook);
        return ShipCashBook;*/
}
