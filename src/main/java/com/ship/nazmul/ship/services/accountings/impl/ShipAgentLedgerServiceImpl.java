//package com.ship.nazmul.ship.services.accountings.impl;
//
//import com.ship.nazmul.ship.commons.PageAttr;
//import com.ship.nazmul.ship.entities.Ship;
//import com.ship.nazmul.ship.entities.User;
//import com.ship.nazmul.ship.entities.accountings.ShipAgentLedger;
//import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
//import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
//import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
//import com.ship.nazmul.ship.repositories.accountings.ShipAgentLedgerRepository;
//import com.ship.nazmul.ship.services.UserService;
//import com.ship.nazmul.ship.services.accountings.ShipAgentLedgerService;
//import com.ship.nazmul.ship.services.accountings.ShipCashBookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class ShipAgentLedgerServiceImpl implements ShipAgentLedgerService {
//    private final ShipAgentLedgerRepository shipAgentLedgerRepository;
//    private final UserService userService;
//    private final ShipCashBookService shipCashBookService;
//
//    @Autowired
//    public ShipAgentLedgerServiceImpl(ShipAgentLedgerRepository shipAgentLedgerRepository, UserService userService, ShipCashBookService shipCashBookService) {
//        this.shipAgentLedgerRepository = shipAgentLedgerRepository;
//        this.userService = userService;
//        this.shipCashBookService = shipCashBookService;
//    }
//
//    @Override
//    public ShipAgentLedger save(ShipAgentLedger shipAgentLedger) {
//        return this.shipAgentLedgerRepository.save(shipAgentLedger);
//    }
//
//    @Override
//    public ShipAgentLedger getShipAgentLedger(Long agentId) {
//        return this.shipAgentLedgerRepository.findOne(agentId);
//    }
//
//    @Override
//    public List<ShipAgentLedger> getAllShipAgentLedger() {
//        return this.shipAgentLedgerRepository.findAll();
//    }
//
//    @Override
//    public Page<ShipAgentLedger> getAllShipAgentLedger(int page) {
//        return this.shipAgentLedgerRepository.findAll(PageAttr.getPageRequest(page));
//    }
//
//    @Override
//    public List<ShipAgentLedger> getShipAgentLedgerByAgentId(Long agentId) {
//        return this.shipAgentLedgerRepository.findByAgentId(agentId);
//    }
//
//    @Override
//    public Page<ShipAgentLedger> getShipAgentLedgerByAgentId(Long agentId, int page) {
//        return this.shipAgentLedgerRepository.findByAgentId(agentId, PageAttr.getPageRequestAsc(page));
//    }
//
//    @Override
//    public void deleteShipAgentLedger(Long agentId) {
//        this.shipAgentLedgerRepository.delete(agentId);
//    }
//
//    @Override
//    public boolean isExists(Long id) {
//        return this.shipAgentLedgerRepository.exists(id);
//    }
//
//    @Override
//    public ShipAgentLedger updateBalanceAndSave(Long agentId, ShipAgentLedger shipAgentLedger) {
//        ShipAgentLedger lastShipAgentLedger = this.shipAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
//        int lastBalance = lastShipAgentLedger == null ? 0 : lastShipAgentLedger.getBalance();
//        shipAgentLedger.setBalance(lastBalance - shipAgentLedger.getDebit() + shipAgentLedger.getCredit());
//        return  this.save(shipAgentLedger);
//    }
//
//
//    /*To add a balance to agent id
//     * 1) Add amount debit to ship cashbook
//     * 2) Add amount credit to ship agent ledger
//     * 3)
//     * */
//    @Override
//    @Transactional
//    public ShipAgentLedger addBalanceToShipAgent(Long agentId,  Long shipId, int amount) throws ForbiddenException, NotFoundException {
//        User user = this.userService.getOne(agentId);
//        Ship ship = user.getShips().iterator().next();
//        if(user == null || ship == null) throw new ForbiddenException("A slap on you left face, idiot");
//
//        //1) Add amount debit to ship cashbook
//        this.shipCashBookService.addShipCashbookEntry(ship.getId(), amount, 0,"Add balance for Agent : " + user.getName() );
//
//        // 2) Add amount credit to ship agent ledger
//        ShipAgentLedger shipAgentLedger = new ShipAgentLedger(ship, user, new Date(), "Add balance", 0, amount);
//        shipAgentLedger = this.updateBalanceAndSave(agentId, shipAgentLedger);
//
//        return shipAgentLedger;
//    }
//
//    @Override
//    public ShipAgentLedger getAgentLastLedger(Long agentId) {
//        return this.shipAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
//    }
//
//    @Override
//    public int getServiceAgentBalance(Long agentId) {
//        ShipAgentLedger shipAgentLedger = this.shipAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
//        return shipAgentLedger.getBalance();
//    }
//}
