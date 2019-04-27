package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.AdminAgentLedger;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminAgentLedgerService {
    AdminAgentLedger save(AdminAgentLedger adminAgentLedger);

    AdminAgentLedger getAdminAgentLedger(Long agentId);

    List<AdminAgentLedger> getAllAdminAgentLedger();

    Page<AdminAgentLedger> getAllAdminAgentLedger(int page);

    List<AdminAgentLedger> getAdminAgentLedgerByAgentId(Long agentId);

    Page<AdminAgentLedger> getAdminAgentLedgerByAgentId(Long agentId, int page);

    void deleteAdminAgentLedger(Long agentId);

    boolean isExists(Long id);

    AdminAgentLedger updateBalanceAndSave(Long agentId, AdminAgentLedger adminAgentLedger);

    AdminAgentLedger addBalanceToAdminAgent(Long agentId, int amount) throws ForbiddenException, UserNotFoundException;

    AdminAgentLedger getAgentLastLedger(Long agentId);

    int getAdminAgentBalance(Long agentId);
}