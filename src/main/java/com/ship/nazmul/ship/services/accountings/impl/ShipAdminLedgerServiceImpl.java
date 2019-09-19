package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.accountings.ShipAdminCashbook;
import com.ship.nazmul.ship.entities.accountings.ShipAdminLedger;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.repositories.accountings.ShipAdminLedgerRepository;
import com.ship.nazmul.ship.services.UserService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import com.ship.nazmul.ship.services.accountings.ShipAdminCashbookService;
import com.ship.nazmul.ship.services.accountings.ShipAdminLedgerService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShipAdminLedgerServiceImpl implements ShipAdminLedgerService {
    private final ShipAdminLedgerRepository shipAdminLedgerRepository;
    private final ShipAdminCashbookService shipAdminCashbookService;
    private final AdminCashbookService adminCashbookService;
    private final UserService userService;

    @Autowired
    public ShipAdminLedgerServiceImpl(ShipAdminLedgerRepository shipAdminLedgerRepository, ShipAdminCashbookService shipAdminCashbookService, AdminCashbookService adminCashbookService, UserService userService) {
        this.shipAdminLedgerRepository = shipAdminLedgerRepository;
        this.shipAdminCashbookService = shipAdminCashbookService;
        this.adminCashbookService = adminCashbookService;
        this.userService = userService;
    }

    /* Save ShipAdminLedger to the database and return with id
    * @param ShipAdminLedger    to be save into the database
    * @return ShipAdminLedger   saved object from database
    * */
    @Override
    public ShipAdminLedger save(ShipAdminLedger shipAdminLedger) {
        return this.shipAdminLedgerRepository.save(shipAdminLedger);
    }

    /*Retrieve ShipAdminLedger from database using object id
    * @param id                     object id to be retrieve
    * @return ShipAdminLedger       object by provided id
    * */
    @Override
    public ShipAdminLedger getShipAdminLedger(Long id) throws NotFoundException {
        if(this.isExists(id)) {
            return this.shipAdminLedgerRepository.findOne(id);
        }
        throw new NotFoundException("ShipAdminLedger with id " + id + " not found");
    }

    /*Get all ShipAdminLedger List from database
    * @return List<ShipAdminLedger>      Retrieve all ShipAdminLedger list from database
    * */
    @Override
    public List<ShipAdminLedger> getAllShipAdminLedger() {
        return this.shipAdminLedgerRepository.findAll();
    }

    /*Get all ShipAdminLedger by pagination from database
    * @return  Page<ShipAdminLedger>      pagination format from database
    * */
    @Override
    public Page<ShipAdminLedger> getAllShipAdminLedger(int page) {
        return this.shipAdminLedgerRepository.findAll(PageAttr.getPageRequest(page));
    }

    /*Get ShipAdminLedger list from database using ShipAdmin Id
    * @param adminId                     to be retrieve from database
    * @return  List<ShipAdminLedger>     form database filter by adminId
    * */
    @Override
    public List<ShipAdminLedger> getShipAdminLedgerByAdminId(Long adminId) {
        return this.shipAdminLedgerRepository.findByUserId(adminId);
    }

    /*Get ShipAdminLedger page from database using ShipAdmin Id
     * @param adminId                   to be retrieve from database
     * @return  Page<ShipAdminLedger    form database filter by adminId
     * */
    @Override
    public Page<ShipAdminLedger> getShipAdminLedgerByAdminId(Long adminId, Integer page) {
        return this.shipAdminLedgerRepository.findByUserIdOrderByIdAsc(adminId, PageAttr.getPageRequest(page));
    }

    /*Delete ShipAdminLedger from database
    * @param id     that to be delete from database
    * */
    @Override
    public void deleteShipAdminLedger(Long id) {
        this.shipAdminLedgerRepository.delete(id);
    }

    /*Find ShipAdminLedger by id if this id exist into the table
    * @param id     that to find into database
    * @return true  if this id exist into the table
    * */
    @Override
    public boolean isExists(Long id) {
        return this.shipAdminLedgerRepository.exists(id);
    }

    /*Update ShipAdminLedger in our database
    * @param id                 that to be update from database
    * @param shipAdminLedger    new object that to be update
    * @return ShipAdminLedger   updated new object
    * */
    @Override
    public ShipAdminLedger addShipAdminLedger(Long adminId, ShipAdminLedger shipAdminLedger) throws NotFoundException {
        shipAdminLedger.setBalance(this.getShipAdminLastBalance(adminId) - shipAdminLedger.getDebit() + shipAdminLedger.getCredit());
        shipAdminLedger = this.save(shipAdminLedger);
        return shipAdminLedger;
    }

    /*Super Admin pay to Ship Admin
    * @param adminId                Ship Admin ID that to be pay by super admin
    * @param amount                 Amount to be pay to Ship admin id
    * @return ShipAdminLedger       paid transaction that saved into database
    *
    * STEPS:
    * 1) Credit amount from AdminCashbook
    * 2) Debit Amount to ShipAdminLedger
    * 3) Debit amount to ShipAdminCashbook
    * */
    @Override
    public ShipAdminLedger payToShipAdmin(Long adminId, int amount) throws ForbiddenException, UserNotFoundException, NotFoundException {
        User user = this.userService.getOne(adminId);
        // 1) Credit amount from AdminCashbook
        this.adminCashbookService.addAdminCashbookEntry(0,amount, "Pay to ShipAdmin : " + user.getName() + " : " + user.getPhoneNumber());

        //2) Debit Amount to ShipAdminLedger
        ShipAdminLedger shipAdminLedger = new ShipAdminLedger(user, new Date(), "Got payment from Hotelswave ", amount, 0);
        shipAdminLedger = this.addShipAdminLedger(adminId, shipAdminLedger);

        //3) Debit amount to ShipAdminCashbook
        ShipAdminCashbook shipAdminCashbook = new ShipAdminCashbook(new Date(), user, "Hotelswave payment ", amount, 0);
        this.shipAdminCashbookService.debitAmount(shipAdminCashbook);

        return shipAdminLedger;
    }

    private int getShipAdminLastBalance(Long adminId){
        ShipAdminLedger lastShipAdminLedger = this.shipAdminLedgerRepository.findFirstByUserIdOrderByIdDesc(adminId);
        return lastShipAdminLedger == null ? 0 : lastShipAdminLedger.getBalance();
    }
}
