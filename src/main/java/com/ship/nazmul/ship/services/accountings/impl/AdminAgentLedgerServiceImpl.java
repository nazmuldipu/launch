package com.ship.nazmul.ship.services.accountings.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.entities.accountings.AdminAgentLedger;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.repositories.accountings.AdminAgentLedgerRepository;
import com.ship.nazmul.ship.services.UserService;
import com.ship.nazmul.ship.services.accountings.AdminAgentLedgerService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class AdminAgentLedgerServiceImpl implements AdminAgentLedgerService {
    private final AdminAgentLedgerRepository adminAgentLedgerRepository;
    private final AdminCashbookService adminCashbookService;
    private final UserService userService;

    @Autowired
    public AdminAgentLedgerServiceImpl(AdminAgentLedgerRepository adminAgentLedgerRepository, AdminCashbookService adminCashbookService, UserService userService) {
        this.adminAgentLedgerRepository = adminAgentLedgerRepository;
        this.adminCashbookService = adminCashbookService;
        this.userService = userService;
    }

    @Override
    public AdminAgentLedger save(AdminAgentLedger adminAgentLedger) {
        return this.adminAgentLedgerRepository.save(adminAgentLedger);
    }

    @Override
    public AdminAgentLedger getAdminAgentLedger(Long agentId) {
        return this.adminAgentLedgerRepository.findOne(agentId);
    }

    @Override
    public List<AdminAgentLedger> getAllAdminAgentLedger() {
        return this.adminAgentLedgerRepository.findAll();
    }

    @Override
    public Page<AdminAgentLedger> getAllAdminAgentLedger(int page) {
        return this.adminAgentLedgerRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public List<AdminAgentLedger> getAdminAgentLedgerByAgentId(Long agentId) {
        return this.adminAgentLedgerRepository.findByAgentId(agentId);
    }

    @Override
    public Page<AdminAgentLedger> getAdminAgentLedgerByAgentId(Long agentId, int page) {
        return this.adminAgentLedgerRepository.findByAgentId(agentId, PageAttr.getPageRequestAsc(page));
    }

    @Override
    public void deleteAdminAgentLedger(Long agentId) {
        this.adminAgentLedgerRepository.delete(agentId);
    }

    @Override
    public boolean isExists(Long id) {
        return this.adminAgentLedgerRepository.exists(id);
    }

    @Override
    public AdminAgentLedger updateBalanceAndSave(Long agentId, AdminAgentLedger adminAgentLedger) {
        AdminAgentLedger lastAdminAgentLedger = this.adminAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
        int lastBalance = lastAdminAgentLedger == null ? 0 : lastAdminAgentLedger.getBalance();
        adminAgentLedger.setBalance(lastBalance - adminAgentLedger.getDebit() + adminAgentLedger.getCredit());
        return this.save(adminAgentLedger);
    }

    @Override
    public AdminAgentLedger addBalanceToAdminAgent(Long agentId, int amount) throws ForbiddenException, UserNotFoundException {
        User user = this.userService.getOne(agentId);
        if (user == null) throw new ForbiddenException("A slap on you left face, idiot");

        //1) Add amount debit to admin cashbook
        this.adminCashbookService.addAdminCashbookEntry(amount, 0, "Add balance for agent : " + user.getName());

        // 2) Add amount credit to Admin_Agent ledger
        AdminAgentLedger adminAgentLedger = new AdminAgentLedger(user, LocalDate.now(), "Add balance : ", 0, amount);
        adminAgentLedger = this.updateBalanceAndSave(agentId, adminAgentLedger);

        return adminAgentLedger;
    }

    @Override
    public AdminAgentLedger getAgentLastLedger(Long agentId) {
        return this.adminAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
    }

    @Override
    public int getAdminAgentBalance(Long agentId) {
        AdminAgentLedger adminAgentLedger = this.adminAgentLedgerRepository.findFirstByAgentIdOrderByIdDesc(agentId);
        if(adminAgentLedger != null) {
            return adminAgentLedger.getBalance();
        }

        return  0;
    }


}