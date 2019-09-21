package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.accountings.ShipAgentLedger;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.repositories.accountings.ShipAgentLedgerRepository;
import com.ship.nazmul.ship.services.UserService;
import com.ship.nazmul.ship.services.accountings.ShipAdminCashbookService;
import com.ship.nazmul.ship.services.accountings.ShipAgentLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class ShipAgentLedgerServiceImpl implements ShipAgentLedgerService {
    private final ShipAgentLedgerRepository shipAgentLedgerRepository;
    private final UserService userService;
    private final ShipAdminCashbookService shipAdminCashbookService;
    //    private final ShipCashBookService shipCashBookService;
//
    @Autowired
    public ShipAgentLedgerServiceImpl(ShipAgentLedgerRepository shipAgentLedgerRepository, UserService userService, ShipAdminCashbookService shipAdminCashbookService) {
        this.shipAgentLedgerRepository = shipAgentLedgerRepository;
        this.userService = userService;
        this.shipAdminCashbookService = shipAdminCashbookService;
    }

    @Override
    public ShipAgentLedger save(ShipAgentLedger shipAgentLedger) {
        return this.shipAgentLedgerRepository.save(shipAgentLedger);
    }

    @Override
    public ShipAgentLedger getShipAgentLedger(Long agentId) {
        return this.shipAgentLedgerRepository.findOne(agentId);
    }

    @Override
    public List<ShipAgentLedger> getAllShipAgentLedger() {
        return this.shipAgentLedgerRepository.findAll();
    }

    @Override
    public Page<ShipAgentLedger> getAllShipAgentLedger(int page) {
        return this.shipAgentLedgerRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public List<ShipAgentLedger> getShipAgentLedgerByAgentId(Long agentId) {
        return this.shipAgentLedgerRepository.findByAgentId(agentId);
    }

    @Override
    public Page<ShipAgentLedger> getShipAgentLedgerByAgentId(Long agentId, int page) {
        return this.shipAgentLedgerRepository.findByAgentId(agentId, PageAttr.getPageRequestAsc(page));
    }

    @Override
    public void deleteShipAgentLedger(Long agentId) {
        this.shipAgentLedgerRepository.delete(agentId);
    }

    @Override
    public boolean isExists(Long id) {
        return this.shipAgentLedgerRepository.exists(id);
    }

    @Override
    public ShipAgentLedger updateBalanceAndSave(Long agentId, ShipAgentLedger shipAgentLedger) {
        ShipAgentLedger lastShipAgentLedger = this.shipAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
        int lastBalance = lastShipAgentLedger == null ? 0 : lastShipAgentLedger.getBalance();
        shipAgentLedger.setBalance(lastBalance - shipAgentLedger.getDebit() + shipAgentLedger.getCredit());
        return  this.save(shipAgentLedger);
    }


    /*To add a balance to agent id
     * Steps :
     * 1) Debit amount to ShipAdmin Cashbook
     * 2) Credit amount to ShipAgent cashbook
     * */
    @Override
    @Transactional
    public ShipAgentLedger addBalanceToShipAgent(Long agentId, int amount) throws UserNotFoundException, ForbiddenException {
        User currentUser = SecurityConfig.getCurrentUser();
        User user = this.userService.getOne(agentId);
        if(!currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) || !user.hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString()))
            throw new ForbiddenException("Only Ship admin can add balance to ship agent");

        // 1) Debit amount to ShipAdmin Cashbook
        this.shipAdminCashbookService.addShipAdminCashbookEntry(currentUser.getId(), amount, 0, "Agent balance from : "+ user.getName() + " ["+user.getPhoneNumber()+"]");

        // 2) Credit amount to ShipAgent cashbook
        ShipAgentLedger shipAgentLedger = new ShipAgentLedger(user, new Date(), "Add balance", 0, amount);
        shipAgentLedger = this.updateBalanceAndSave(agentId, shipAgentLedger);

        return shipAgentLedger;
    }

    @Override
    public ShipAgentLedger getAgentLastLedger(Long agentId) {
        return this.shipAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
    }

    @Override
    public int getServiceAgentBalance(Long agentId) {
        return this.shipAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId).getBalance();
    }
}
